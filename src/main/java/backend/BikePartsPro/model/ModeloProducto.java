package backend.BikePartsPro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modelos_producto")

public class ModeloProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
