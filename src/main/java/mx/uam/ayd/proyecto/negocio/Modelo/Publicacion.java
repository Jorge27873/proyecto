package mx.uam.ayd.proyecto.negocio.Modelo;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPublicacion;
    private String descripcion;
    private String locacion;

    @ManyToOne
    private Usuario usuario;
}