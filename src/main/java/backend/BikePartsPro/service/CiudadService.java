package backend.BikePartsPro.service;

import backend.BikePartsPro.DTO.CiudadRequestDTO;
import backend.BikePartsPro.model.Ciudad;
import backend.BikePartsPro.model.Departamento;
import backend.BikePartsPro.repository.CiudadRepository;
import backend.BikePartsPro.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;

    public CiudadService(CiudadRepository ciudadRepository,
                         DepartamentoRepository departamentoRepository) {
        this.ciudadRepository = ciudadRepository;
        this.departamentoRepository = departamentoRepository;
    }

    public List<Ciudad> findAll() {
        return ciudadRepository.findAll();
    }

    public Ciudad findById(Long id) {
        return ciudadRepository.findById(id).orElse(null);
    }

    public Ciudad save(CiudadRequestDTO dto) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(dto.getNombre());
        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId()).orElse(null);
        ciudad.setDepartamento(departamento);
        return ciudadRepository.save(ciudad);
    }

    public Ciudad update(Long id, CiudadRequestDTO dto) {
        Ciudad existente = ciudadRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(dto.getNombre());
        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId()).orElse(null);
        existente.setDepartamento(departamento);
        return ciudadRepository.save(existente);
    }

    public void delete(Long id) {
        ciudadRepository.deleteById(id);
    }
}
