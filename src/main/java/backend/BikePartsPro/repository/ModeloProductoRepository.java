package backend.BikePartsPro.repository;

import backend.BikePartsPro.model.ModeloProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloProductoRepository extends JpaRepository<ModeloProducto, Long> {
}
