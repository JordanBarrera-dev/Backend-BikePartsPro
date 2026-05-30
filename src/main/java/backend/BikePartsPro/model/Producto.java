package backend.BikePartsPro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre tiene que estar")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "modelo_producto_id")
    private ModeloProducto modeloProducto;

    @OneToMany(
            mappedBy = "producto",
            cascade = CascadeType.ALL
    )
    private list<ImagenProducto> imagenes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;

    public Producto() {
    }

    public Producto(Long id, String nombre, Double precio, Integer stock, CategoriaProducto categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public ModeloProducto getModeloProducto() {
        return modeloProducto;
    }

    public void setModeloProducto(ModeloProducto modeloProducto) {
        this.modeloProducto = modeloProducto;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }
}
