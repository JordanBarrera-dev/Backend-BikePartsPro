package backend.BikePartsPro.service;

import backend.BikePartsPro.model.Departamento;
import backend.BikePartsPro.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Departamento findById(Long id) {
        return departamentoRepository.findById(id).orElse(null);
    }

    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    public Departamento update(Long id, Departamento datos) {
        Departamento existente = departamentoRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(datos.getNombre());
        return departamentoRepository.save(existente);
    }

    public void delete(Long id) {
        departamentoRepository.deleteById(id);
    }
}
