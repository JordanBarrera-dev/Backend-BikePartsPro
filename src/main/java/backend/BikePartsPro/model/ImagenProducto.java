package backend.BikePartsPro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_producto" )

public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 1000)
    private String url;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
