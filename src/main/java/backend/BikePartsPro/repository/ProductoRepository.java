package backend.BikePartsPro.repository;

import backend.BikePartsPro.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import backend.BikePartsPro.model.CategoriaProducto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(
            CategoriaProducto categoria
    );

    List<Producto>
    findByModeloProductoNombreContainingIgnoreCase(
            String palabra
    );
}
