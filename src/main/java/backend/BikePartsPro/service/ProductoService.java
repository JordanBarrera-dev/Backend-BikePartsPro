package backend.BikePartsPro.service;

import backend.BikePartsPro.DTO.ProductoRequestDTO;
import backend.BikePartsPro.model.ImagenProducto;
import backend.BikePartsPro.model.ModeloProducto;
import backend.BikePartsPro.model.Producto;
import backend.BikePartsPro.repository.ModeloProductoRepository;
import backend.BikePartsPro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.BikePartsPro.model.CategoriaProducto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ModeloProductoRepository modeloProductoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, ModeloProductoRepository modeloProductoRepository) {
        this.productoRepository = productoRepository;
        this.modeloProductoRepository = modeloProductoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findByStockGreaterThan(0);
    }

    public List<Producto> findByCategoria(CategoriaProducto categoria) {
        return productoRepository.findByCategoriaAndStockGreaterThan(categoria, 0);
    }

    public List<Producto> buscarPorPalabra(String palabra) {
        return productoRepository.buscarFiltrado(null, toPatron(palabra));
    }

    public Producto findById(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto crear(ProductoRequestDTO dto) {
        ModeloProducto modelo = modeloProductoRepository.findById(dto.getModeloProductoId()).orElse(null);
        if (modelo == null) return null;

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getStock() > 0);
        producto.setCategoria(dto.getCategoria());
        producto.setModeloProducto(modelo);

        Producto guardado = productoRepository.save(producto);

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            List<ImagenProducto> imagenes = new ArrayList<>();
            for (String url : dto.getImagenes()) {
                ImagenProducto img = new ImagenProducto();
                img.setUrl(url);
                img.setProducto(guardado);
                imagenes.add(img);
            }
            guardado.setImagenes(imagenes);
            guardado = productoRepository.save(guardado);
        }

        return guardado;
    }

    public Producto update(Long id, Producto datos) {
        Producto existente = productoRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        if (datos.getStock() == 0) {
            existente.setActivo(false);
        } else {
            existente.setActivo(true);
        }
        existente.setCategoria(datos.getCategoria());
        return productoRepository.save(existente);
    }

    public List<Producto> buscarFiltrado(CategoriaProducto categoria, String palabra) {
        return productoRepository.buscarFiltrado(categoria, toPatron(palabra));
    }

    private String toPatron(String palabra) {
        return "%" + (palabra == null ? "" : palabra.toLowerCase()) + "%";
    }

    public void delete(Long id){
        productoRepository.deleteById(id);
    }

}
