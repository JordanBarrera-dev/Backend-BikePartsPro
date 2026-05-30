package backend.BikePartsPro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "modelos_producto")

public class ModeloProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 6, max = 6,
            message = "El SKU debe tener exactamente 6 caracteres")
    @Column(nullable = false, unique = true, length = 6)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

}
