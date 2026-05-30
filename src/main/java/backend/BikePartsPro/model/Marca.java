package backend.BikePartsPro.model;

import jakarta.persistence.*;

@Entity
@Table (name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
}





