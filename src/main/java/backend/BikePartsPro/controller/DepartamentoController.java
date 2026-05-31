package backend.BikePartsPro.controller;

import backend.BikePartsPro.model.Departamento;
import backend.BikePartsPro.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> obtenerTodos() {
        return ResponseEntity.ok(departamentoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> obtenerPorId(@PathVariable Long id) {
        Departamento departamento = departamentoService.findById(id);
        if (departamento == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(departamento);
    }

    @PostMapping
    public ResponseEntity<Departamento> crear(@RequestBody Departamento departamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.save(departamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> actualizar(@PathVariable Long id, @RequestBody Departamento datos) {
        Departamento actualizado = departamentoService.update(id, datos);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        departamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
