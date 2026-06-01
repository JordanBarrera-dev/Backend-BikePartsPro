package backend.BikePartsPro.service;

import backend.BikePartsPro.DTO.EnvioRequestDTO;
import backend.BikePartsPro.model.Ciudad;
import backend.BikePartsPro.model.Cliente;
import backend.BikePartsPro.model.Envio;
import backend.BikePartsPro.repository.CiudadRepository;
import backend.BikePartsPro.repository.ClienteRepository;
import backend.BikePartsPro.repository.EnvioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ciudad no encontrada: " + dto.getCiudadId()));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + dto.getClienteId()));

        Envio envio = new Envio();
        envio.setNombreRecibe(dto.getNombreRecibe());
        envio.setDireccion(dto.getDireccion());
        envio.setComplemento(dto.getComplemento());
        envio.setCodigoPostal(dto.getCodigoPostal());
        envio.setCiudad(ciudad);
        envio.setCliente(cliente);

        return envioRepository.save(envio);
    }

    public Envio update(Long id, EnvioRequestDTO dto) {
        Envio existente = envioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Envío no encontrado: " + id));

        Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ciudad no encontrada: " + dto.getCiudadId()));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + dto.getClienteId()));

        existente.setNombreRecibe(dto.getNombreRecibe());
        existente.setDireccion(dto.getDireccion());
        existente.setComplemento(dto.getComplemento());
        existente.setCodigoPostal(dto.getCodigoPostal());
        existente.setCiudad(ciudad);
        existente.setCliente(cliente);

        return envioRepository.save(existente);
    }

    public void delete(Long id) {
        envioRepository.deleteById(id);
    }
}
