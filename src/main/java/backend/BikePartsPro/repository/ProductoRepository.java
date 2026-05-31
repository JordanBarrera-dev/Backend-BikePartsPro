package backend.BikePartsPro.repository;

import backend.BikePartsPro.model.CategoriaProducto;
import backend.BikePartsPro.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(CategoriaProducto categoria);

    @Query("SELECT p FROM Producto p LEFT JOIN p.modeloProducto mp WHERE " +
           "(:categoria IS NULL OR p.categoria = :categoria) AND " +
           "(LOWER(p.nombre) LIKE :patron OR LOWER(mp.nombre) LIKE :patron)")
    List<Producto> buscarFiltrado(
            @Param("categoria") CategoriaProducto categoria,
            @Param("patron") String patron
    );
}
