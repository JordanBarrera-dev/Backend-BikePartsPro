package backend.BikePartsPro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_producto" )

public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 1000)
    private String url;

    @JsonBackReference("producto-imagenes")
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public ImagenProducto() {
    }

    public ImagenProducto(
            Long id,
            String url,
            Producto producto
    ) {
        this.id = id;
        this.url = url;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
