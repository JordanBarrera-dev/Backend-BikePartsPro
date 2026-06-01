package backend.BikePartsPro.controller;

import backend.BikePartsPro.DTO.OrdenRequestDTO;
import backend.BikePartsPro.DTO.OrdenResponseDTO;
import backend.BikePartsPro.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    @Autowired
    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(ordenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> obtenerPorId(@PathVariable Long id) {
        OrdenResponseDTO orden = ordenService.findById(id);
        if (orden == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orden);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrdenResponseDTO> checkout(@Valid @RequestBody OrdenRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenService.checkout(dto));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrdenResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
