package backend.BikePartsPro.service;

import backend.BikePartsPro.model.Marca;
import backend.BikePartsPro.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    public Marca save(Marca marca) {
        return marcaRepository.save(marca);
    }

    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }
}
