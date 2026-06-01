package backend.BikePartsPro.repository;

import backend.BikePartsPro.model.CategoriaProducto;
import backend.BikePartsPro.model.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Solo productos con stock disponible
    List<Producto> findByStockGreaterThan(Integer stock);

    List<Producto> findByCategoriaAndStockGreaterThan(CategoriaProducto categoria, Integer stock);

    // Filtro combinado solo con stock > 0
    @Query("SELECT p FROM Producto p LEFT JOIN p.modeloProducto mp WHERE " +
           "(:categoria IS NULL OR p.categoria = :categoria) AND " +
           "(LOWER(p.nombre) LIKE :patron OR LOWER(mp.nombre) LIKE :patron) AND " +
           "p.stock > 0")
    List<Producto> buscarFiltrado(
            @Param("categoria") CategoriaProducto categoria,
            @Param("patron") String patron
    );

    // Bloqueo pesimista para evitar race conditions al descontar stock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdWithLock(@Param("id") Long id);

    // Descuento directo en base de datos con UPDATE atómico
    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad, p.activo = CASE WHEN (p.stock - :cantidad) > 0 THEN true ELSE false END WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);
}
