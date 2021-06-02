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
        if(usuario != null) {
            throw new IllegalArgumentException("Ese usuario ya existe");
        }
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
     * Recupera todos un usuario de la base de datos
     * @param id id del usuario
     * @return un usuario
     */
    public UsuarioDto muestraUsuario(long id){
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isEmpty()){throw new IllegalArgumentException("Usuario inexistente");}
        Usuario usuario = usuarioOpt.get();
        log.info("Mi nombre es en muestra"+usuario.getNombreDeUsuario());
        log.info("Mi desc es en muestra"+usuario.getDescripcion());
        return UsuarioDto.creaUsuario(usuario);
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
}