package backend.BikePartsPro.controller;


import backend.BikePartsPro.model.Producto;
import backend.BikePartsPro.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    //@GetMapping
//    public List<Producto> obtenerTodos() {
//        return productoService.findAll();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody Producto datos) {
        Producto actualizado = productoService.update(id, datos);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


//
//@RestController
//@RequestMapping("/productos")
//public class ProductoController {
//    private final ProductoService productoService;
//
//    @Autowired
//    public ProductoController(ProductoService productoService) {
//        this.productoService = productoService;
//    }
//
//    @GetMapping
//    public List<Producto> obtenerTodos() {
//        return productoService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Producto obtenerPorId(@PathVariable Long id){
//        return productoService.findById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Producto actualizar(@PathVariable Long id, @RequestBody Producto datos){
//        return productoService.update(id, datos);
//    }
//
//    @PostMapping
//    public Producto crear(@RequestBody Producto producto){
//        return productoService.save(producto);
//    }
//
//    @DeleteMapping("/{id}")
//    public  void eliminar(@PathVariable Long id){
//        productoService.delete(id);
//    }
//}