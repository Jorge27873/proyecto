package mx.uam.ayd.proyecto.negocio;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.datos.PublicacionRepository;
import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.dto.PublicacionDto;
import mx.uam.ayd.proyecto.dto.UsuarioDto;
import mx.uam.ayd.proyecto.negocio.Modelo.Publicacion;
import mx.uam.ayd.proyecto.negocio.Modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ServicioUsuario {
    @Autowired
    UsuarioRepository repositorioUsuario;

    @Autowired
    PublicacionRepository repositorioPublicacion;

    /**
     * Crea un usuario nuevo
     *
     * @param usuarioDto usuario a crear
     * @return la confirmacion del usuario creado
     */
    public UsuarioDto agregaUsuario(UsuarioDto usuarioDto) {
        // Regla de negocio: No se permite agregar dos usuarios con el mismo nombre de usuario
        Usuario usuario = repositorioUsuario.findBynombreDeUsuario(usuarioDto.getNombreDeUsuario());
        if(usuario != null) {throw new IllegalArgumentException("Ese usuario ya existe");}
        log.info("Agregando usuario nombre: "+usuarioDto.getNombreDeUsuario());
        usuario = new Usuario();
        usuario.setNombreDeUsuario(usuarioDto.getNombreDeUsuario());
        log.info("Mi nombre es "+usuario.getNombreDeUsuario());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setDescripcion(usuarioDto.getDescripcion());
        log.info("Mi desc es "+usuario.getDescripcion());
        usuario.setPasswrd(usuarioDto.getPasswrd());
        repositorioUsuario.save(usuario);
        usuarioDto.setIdUsuario(usuario.getIdUsuario());
        return usuarioDto;
    }

    /**
     * Muestra un usuario por su nombre
     *
     * @param id nombre del usuario a buscar
     * @return el usuario encontrado
     */
    public UsuarioDto muestraUsuarioNombre(String id){
        Usuario usuario = repositorioUsuario.findBynombreDeUsuario(id);
        if (usuario == null){throw new IllegalArgumentException("Usuario inexistente");}
        return UsuarioDto.creaUsuario(usuario);
    }

    /**
     * Recupera todas las publicaciones de un usuario
     *
     * @param id usuario a buscar las publicaciones
     * @return la la lista de publicaciones
     */
    public List<PublicacionDto> recuperaPosts(long id){
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        Usuario usuario = usuarioOpt.get();
        List<PublicacionDto> publicaciones = new ArrayList<>();
        for (Publicacion publicacion:usuario.getPublicaciones()) {
            publicaciones.add(PublicacionDto.creaPublicacionDto(publicacion));
        }
        return publicaciones;
    }

    public PublicacionDto recuperaPost(long id,long idPub){
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        Usuario usuario = usuarioOpt.get();
        Optional<Publicacion> publicacionOpt = repositorioPublicacion.findById(idPub);
        if (publicacionOpt.isEmpty()){throw new IllegalArgumentException("Publicacion inexistente");}
        Publicacion publicacion = publicacionOpt.get();
        if (usuario.getIdUsuario() != publicacion.getUsuario().getIdUsuario()){ throw new IllegalArgumentException("El usuario no tiene esta publicacion");}
        return PublicacionDto.creaPublicacionDto(publicacion);
    }

    public PublicacionDto agregaPublicacion(long id, PublicacionDto publicacionDto){
        //revisamos si el usuario existe
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        Usuario usuario = usuarioOpt.get();
        Publicacion publicacion = new Publicacion();
        publicacion.setUsuario(usuario);
        publicacion.setDescripcion(publicacionDto.getDescripcion());
        publicacion.setLocacion(publicacionDto.getLocacion());
        repositorioPublicacion.save(publicacion);
        usuario.addPublicacion(publicacion);
        repositorioUsuario.save(usuario);
        return PublicacionDto.creaPublicacionDto(publicacion);
    }

    public void borraPublicacion(long id, long idPost){
        //revisamos si el usuario existe
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        //revisamos si el post existe
        Optional<Publicacion> pubOpt = repositorioPublicacion.findById(idPost);
        if (pubOpt.isEmpty()){throw new IllegalArgumentException("Publicacion inexistente");}
        Usuario usuario = usuarioOpt.get();
        Publicacion publicacion = pubOpt.get();
        usuario.removePublicacion(publicacion);
        repositorioPublicacion.delete(publicacion);
        repositorioUsuario.save(usuario);
    }

    public void actualizaPublicacion(long id, long idPost, PublicacionDto publicacionDto){
        //revisamos si el usuario existe
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        //revisamos si el post existe
        Optional<Publicacion> pubOpt = repositorioPublicacion.findById(idPost);
        if (pubOpt.isEmpty()){throw new IllegalArgumentException("Publicacion inexistente");}
        Usuario usuario = usuarioOpt.get();
        Publicacion publicacion = pubOpt.get();
        usuario.removePublicacion(publicacion);
        publicacion.setLocacion(publicacionDto.getLocacion());
        publicacion.setDescripcion(publicacionDto.getDescripcion());
        usuario.addPublicacion(publicacion);
        repositorioPublicacion.save(publicacion);
        repositorioUsuario.save(usuario);
    }
}