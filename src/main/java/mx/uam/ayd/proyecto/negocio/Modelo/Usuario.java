package mx.uam.ayd.proyecto.negocio.Modelo;

//import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entidad de negocio Usuario
 *
 *
 */
@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUsuario;

    private String nombreDeUsuario;
    private String descripcion;
    private String correo;
    private String passwrd;
    //@NotNull
    private String imagen;

    @OneToMany(targetEntity = Publicacion.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idUsuario")
    private final List<Publicacion> publicaciones = new ArrayList<>();

    /**
     * Agrega una publicacion a la lista de usuarios
     *
     * @param publicacion publicacion a eliminar
     * @return true si se agrega el usuario y false si ya se encontraba la publicacion
     */
    public boolean addPublicacion(Publicacion publicacion) {
        if(publicacion == null) {throw new IllegalArgumentException("Publicacion inexistente");}
        return publicaciones.add(publicacion);
    }

    /**
     *
     * @param publicacion publicacion a eliminar
     * @return true si la publicacion se remueve, false si la publicacion no existe o no se encuentra
     */
    public boolean removePublicacion(Publicacion publicacion){
        if(publicacion == null) {throw new IllegalArgumentException("Publicacion inexistente");}
        if (!publicaciones.contains(publicacion)){return false;}
        return publicaciones.remove(publicacion);
    }

    public List <Long> recuperaIds(){
        List <Long> ids = new ArrayList<>();
        for (Publicacion publicacion: publicaciones) {
            ids.add(publicacion.getIdPublicacion());
        }
        return ids;
    }
}