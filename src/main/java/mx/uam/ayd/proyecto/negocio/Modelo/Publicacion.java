package mx.uam.ayd.proyecto.negocio.Modelo;

//import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPublicacion;
  //  @NotNull
    private String descripcion;
    //@NotNull
    private String locacion;
    //@NotNull
    private String imagen;

    @ManyToOne
    private Usuario usuario;
}