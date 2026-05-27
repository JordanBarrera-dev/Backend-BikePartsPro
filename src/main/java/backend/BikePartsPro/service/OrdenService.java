package backend.BikePartsPro.service;



import backend.BikePartsPro.DTO.OrdenRequestDTO;
import backend.BikePartsPro.DTO.OrdenResponseDTO;
import backend.BikePartsPro.model.Cliente;
import backend.BikePartsPro.model.Orden;
import backend.BikePartsPro.repository.ClienteRepository;
import backend.BikePartsPro.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public OrdenService(OrdenRepository ordenRepository,
                        ClienteRepository clienteRepository) {
        this.ordenRepository = ordenRepository;
        this.clienteRepository = clienteRepository;
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

    public OrdenResponseDTO save(OrdenRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
        Orden orden = new Orden(dto.getFecha(), dto.getEstado(), cliente);
        return OrdenResponseDTO.desde(ordenRepository.save(orden));
    }

    public OrdenResponseDTO update(Long id, OrdenRequestDTO dto) {
        Orden existente = ordenRepository.findById(id).orElse(null);
        if (existente == null) return null;
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
        existente.setFecha(dto.getFecha());
        existente.setEstado(dto.getEstado());
        existente.setCliente(cliente);
        return OrdenResponseDTO.desde(ordenRepository.save(existente));
    }

    public void delete(Long id) {
        ordenRepository.deleteById(id);
    }
}


//
//@Service
//public class OrdenService {
//
//    private final OrdenRepository ordenRepository;
//    private final ClienteRepository clienteRepository;
//
//    @Autowired
//    public OrdenService(OrdenRepository ordenRepository, ClienteRepository clienteRepository) {
//        this.ordenRepository = ordenRepository;
//        this.clienteRepository = clienteRepository;
//    }
//
//    public List<Orden> findAll() {
//        return ordenRepository.findAll();
//    }
//
//    public Orden findById(Long id) {
//        return ordenRepository.findById(id).orElse(null);
//    }
//
//    public Orden save(Orden orden) {
//        return ordenRepository.save(orden);
//    }
////    public Orden save(Orden orden) {
////        Cliente cliente = clienteRepository.findById(orden.getCliente().getId()).orElse(null);
////        orden.setCliente(cliente);
////        return ordenRepository.save(orden);
////    }
//
//    public Orden update(Long id, Orden datos) {
//        Orden existente = ordenRepository.findById(id).orElse(null);
//        if (existente == null) return null;
//        existente.setFecha(datos.getFecha());
//        existente.setEstado(datos.getEstado());
//        existente.setCliente(datos.getCliente());
//        return ordenRepository.save(existente);
//    }
//
//    public void delete(Long id) {
//        ordenRepository.deleteById(id);
//    }
//}
