package backend.BikePartsPro.service;

import backend.BikePartsPro.DTO.OrdenItemRequestDTO;
import backend.BikePartsPro.DTO.OrdenRequestDTO;
import backend.BikePartsPro.DTO.OrdenResponseDTO;
import backend.BikePartsPro.model.*;
import backend.BikePartsPro.repository.ClienteRepository;
import backend.BikePartsPro.repository.OrdenItemRepository;
import backend.BikePartsPro.repository.OrdenRepository;
import backend.BikePartsPro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final OrdenItemRepository ordenItemRepository;

    @Autowired
    public OrdenService(OrdenRepository ordenRepository,
                        ClienteRepository clienteRepository,
                        ProductoRepository productoRepository,
                        OrdenItemRepository ordenItemRepository) {
        this.ordenRepository = ordenRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.ordenItemRepository = ordenItemRepository;
    }

    public List<OrdenResponseDTO> findAll() {
        return ordenRepository.findAll()
                .stream()
                .map(OrdenResponseDTO::desde)
                .collect(Collectors.toList());
    }

    public OrdenResponseDTO findById(Long id) {
        Orden orden = ordenRepository.findById(id).orElse(null);
        if (orden == null) return null;
        return OrdenResponseDTO.desde(orden);
    }

    @Transactional
    public OrdenResponseDTO checkout(OrdenRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + dto.getClienteId()));

        for (OrdenItemRequestDTO itemDto : dto.getItems()) {
            Producto producto = productoRepository.findByIdWithLock(itemDto.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Producto no encontrado: " + itemDto.getProductoId()));

            if (producto.getStock() < itemDto.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para '" + producto.getNombre() +
                        "'. Disponible: " + producto.getStock() +
                        ", solicitado: " + itemDto.getCantidad());
            }
        }

        Orden orden = new Orden(LocalDateTime.now(), EstadoOrden.PAGADA, cliente);
        ordenRepository.save(orden);

        for (OrdenItemRequestDTO itemDto : dto.getItems()) {
            Producto producto = productoRepository.findByIdWithLock(itemDto.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Producto no encontrado: " + itemDto.getProductoId()));

            int filasActualizadas = productoRepository.descontarStock(producto.getId(), itemDto.getCantidad());
            if (filasActualizadas == 0) {
                // Otro hilo compró el último stock justo antes que esta transacción
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock agotado para '" + producto.getNombre() + "' durante el proceso de pago");
            }

            OrdenItem item = new OrdenItem(itemDto.getCantidad(), producto.getPrecio(), orden, producto);
            ordenItemRepository.save(item);
        }

        return OrdenResponseDTO.desde(ordenRepository.findById(orden.getId()).orElseThrow());
    }

    @Transactional
    public OrdenResponseDTO cancelar(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));

        if (orden.getEstado() == EstadoOrden.ENVIADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede cancelar una orden ya enviada");
        }

        if (orden.getEstado() == EstadoOrden.PAGADA || orden.getEstado() == EstadoOrden.PROCESANDO) {
            for (OrdenItem item : orden.getItems()) {
                Producto producto = item.getProducto();
                producto.setStock(producto.getStock() + item.getCantidad());
                if (producto.getStock() > 0) producto.setActivo(true);
                productoRepository.save(producto);
            }
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        return OrdenResponseDTO.desde(ordenRepository.save(orden));
    }

    public void delete(Long id) {
        ordenRepository.deleteById(id);
    }
}
