package backend.BikePartsPro.controller;

import backend.BikePartsPro.DTO.CiudadRequestDTO;
import backend.BikePartsPro.model.Ciudad;
import backend.BikePartsPro.service.CiudadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public ResponseEntity<List<Ciudad>> obtenerTodas() {
        return ResponseEntity.ok(ciudadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerPorId(@PathVariable Long id) {
        Ciudad ciudad = ciudadService.findById(id);
        if (ciudad == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ciudad);
    }

    @PostMapping
    public ResponseEntity<Ciudad> crear(@RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Long id, @RequestBody CiudadRequestDTO dto) {
        Ciudad actualizada = ciudadService.update(id, dto);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ciudadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
