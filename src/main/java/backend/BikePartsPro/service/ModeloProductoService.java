package backend.BikePartsPro.service;

import backend.BikePartsPro.model.Marca;
import backend.BikePartsPro.model.ModeloProducto;
import backend.BikePartsPro.repository.MarcaRepository;
import backend.BikePartsPro.repository.ModeloProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModeloProductoService {

    private final ModeloProductoRepository modeloProductoRepository;
    private final MarcaRepository marcaRepository;

    public ModeloProductoService(ModeloProductoRepository modeloProductoRepository,
                                 MarcaRepository marcaRepository) {
        this.modeloProductoRepository = modeloProductoRepository;
        this.marcaRepository = marcaRepository;
    }

    public List<ModeloProducto> findAll() {
        return modeloProductoRepository.findAll();
    }

    public ModeloProducto findById(Long id) {
        return modeloProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Modelo no encontrado: " + id));
    }

    public ModeloProducto save(Long marcaId, String sku, String nombre, String descripcion) {
        Marca marca = marcaRepository.findById(marcaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Marca no encontrada: " + marcaId));

        ModeloProducto modelo = new ModeloProducto();
        modelo.setSku(sku);
        modelo.setNombre(nombre);
        modelo.setDescripcion(descripcion);
        modelo.setMarca(marca);

        return modeloProductoRepository.save(modelo);
    }

    public void delete(Long id) {
        modeloProductoRepository.deleteById(id);
    }
}
