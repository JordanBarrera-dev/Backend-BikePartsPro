package backend.BikePartsPro.service;

import backend.BikePartsPro.model.Producto;
import backend.BikePartsPro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.BikePartsPro.model.CategoriaProducto;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto>
    findByCategoria(CategoriaProducto categoria){

        return productoRepository
                .findByCategoria(categoria);
    }

    public List<Producto>
    buscarPorPalabra(String palabra){

        return productoRepository
                .findByModeloProductoNombreContainingIgnoreCase(
                        palabra
                );
    }

    public Producto findById(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto datos) {
        Producto existente = productoRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setCategoria(datos.getCategoria());
        return productoRepository.save(existente);
    }

    public void delete(Long id){
        productoRepository.deleteById(id);
    }

}
