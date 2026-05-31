package backend.BikePartsPro.service;

import backend.BikePartsPro.DTO.EnvioRequestDTO;
import backend.BikePartsPro.model.Ciudad;
import backend.BikePartsPro.model.Cliente;
import backend.BikePartsPro.model.Envio;
import backend.BikePartsPro.repository.CiudadRepository;
import backend.BikePartsPro.repository.ClienteRepository;
import backend.BikePartsPro.repository.EnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final CiudadRepository ciudadRepository;
    private final ClienteRepository clienteRepository;

    public EnvioService(EnvioRepository envioRepository,
                        CiudadRepository ciudadRepository,
                        ClienteRepository clienteRepository) {
        this.envioRepository = envioRepository;
        this.ciudadRepository = ciudadRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    public Envio findById(Long id) {
        return envioRepository.findById(id).orElse(null);
    }

    public Envio save(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setNombreRecibe(dto.getNombreRecibe());
        envio.setDireccion(dto.getDireccion());
        envio.setComplemento(dto.getComplemento());
        envio.setCodigoPostal(dto.getCodigoPostal());

        Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId()).orElse(null);
        envio.setCiudad(ciudad);

        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
        envio.setCliente(cliente);

        return envioRepository.save(envio);
    }

    public Envio update(Long id, EnvioRequestDTO dto) {
        Envio existente = envioRepository.findById(id).orElse(null);
        if (existente == null) return null;

        existente.setNombreRecibe(dto.getNombreRecibe());
        existente.setDireccion(dto.getDireccion());
        existente.setComplemento(dto.getComplemento());
        existente.setCodigoPostal(dto.getCodigoPostal());

        Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId()).orElse(null);
        existente.setCiudad(ciudad);

        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
        existente.setCliente(cliente);

        return envioRepository.save(existente);
    }

    public void delete(Long id) {
        envioRepository.deleteById(id);
    }
}
