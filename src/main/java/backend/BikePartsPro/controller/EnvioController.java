package backend.BikePartsPro.controller;

import backend.BikePartsPro.DTO.EnvioRequestDTO;
import backend.BikePartsPro.model.Envio;
import backend.BikePartsPro.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodos() {
        return ResponseEntity.ok(envioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) {
        Envio envio = envioService.findById(id);
        if (envio == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(envio);
    }

    @PostMapping
    public ResponseEntity<Envio> crear(@Valid @RequestBody EnvioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizar(@PathVariable Long id, @Valid @RequestBody EnvioRequestDTO dto) {
        Envio actualizado = envioService.update(id, dto);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
