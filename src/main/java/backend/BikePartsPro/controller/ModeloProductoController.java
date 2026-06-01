package backend.BikePartsPro.controller;

import backend.BikePartsPro.DTO.ModeloProductoRequestDTO;
import backend.BikePartsPro.model.ModeloProducto;
import backend.BikePartsPro.service.ModeloProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modelos-producto")
public class ModeloProductoController {

    private final ModeloProductoService modeloProductoService;

    public ModeloProductoController(ModeloProductoService modeloProductoService) {
        this.modeloProductoService = modeloProductoService;
    }

    @GetMapping
    public ResponseEntity<List<ModeloProducto>> obtenerTodos() {
        return ResponseEntity.ok(modeloProductoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloProducto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modeloProductoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ModeloProducto> crear(@Valid @RequestBody ModeloProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modeloProductoService.save(dto.getMarcaId(), dto.getSku(), dto.getNombre(), dto.getDescripcion()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        modeloProductoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
