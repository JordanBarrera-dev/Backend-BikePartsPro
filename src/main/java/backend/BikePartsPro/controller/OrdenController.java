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

    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crear( @Valid @RequestBody OrdenRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody OrdenRequestDTO dto) {
        OrdenResponseDTO actualizado = ordenService.update(id, dto);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


//@RestController
//@RequestMapping("/ordenes")
//public class OrdenController {
//
//    private final OrdenService ordenService;
//
//    @Autowired
//    public OrdenController(OrdenService ordenService) {
//        this.ordenService = ordenService;
//    }
//
//    @GetMapping
//    public List<OrdenResponseDTO> obtenerTodos() {
//        return ordenService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public OrdenResponseDTO obtenerPorId(@PathVariable Long id) {
//        return ordenService.findById(id);
//    }
//
//    @PostMapping
//    public OrdenResponseDTO crear(@RequestBody OrdenRequestDTO dto) {
//        return ordenService.save(dto);
//    }
//
//    @PutMapping("/{id}")
//    public OrdenResponseDTO actualizar(@PathVariable Long id,
//                                       @RequestBody OrdenRequestDTO dto) {
//        return ordenService.update(id, dto);
//    }
//
//    @DeleteMapping("/{id}")
//    public void eliminar(@PathVariable Long id) {
//        ordenService.delete(id);
//    }
//}


//@RestController
//@RequestMapping("/ordenes")
//public class OrdenController {
//
//    private final OrdenService ordenService;
//
//    @Autowired
//    public OrdenController(OrdenService ordenService) {
//        this.ordenService = ordenService;
//    }
//
//    @GetMapping
//    public List<Orden> obtenerTodos() {
//        return ordenService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Orden obtenerPorId(@PathVariable Long id) {
//        return ordenService.findById(id);
//    }
//
//    @PostMapping
//    public Orden crear(@RequestBody Orden orden) {
//        return ordenService.save(orden);
//    }
//
//    @PutMapping("/{id}")
//    public Orden actualizar(@PathVariable Long id, @RequestBody Orden datos) {
//        return ordenService.update(id, datos);
//    }
//
//    @DeleteMapping("/{id}")
//    public void eliminar(@PathVariable Long id) {
//        ordenService.delete(id);
//    }
//}

