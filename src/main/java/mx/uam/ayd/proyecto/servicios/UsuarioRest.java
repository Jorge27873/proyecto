package mx.uam.ayd.proyecto.servicios;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.dto.PublicacionDto;
import mx.uam.ayd.proyecto.dto.UsuarioDto;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1") // Versioning
@Slf4j
public class UsuarioRest {

    @Autowired
    ServicioUsuario servicioUsuario;

    //  ******************** METODOS GET ********************

    /**
     * Recupera un usuario a partir de su nombre de usuario
     *
     * @param id el id del usuario
     * @return el usuario encontrado
     */
    @GetMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> findUserById(@PathVariable("id") long id){
        try {
            UsuarioDto usuarioDto = servicioUsuario.recuperaUsuario(id);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * Recupera todas la publicaciones de un usuario
     *
     * @param id el id del usuario
     * @return las publicaciones del usuario
     */
    @GetMapping(path = "usuarios/{id}/publicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicacionDto>> showPosts(@PathVariable("id") Long id){
        try {
            List <PublicacionDto> publicaciones = servicioUsuario.recuperaPosts(id);
            return ResponseEntity.status(HttpStatus.OK).body(publicaciones);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * Recupera un post especifico de un usuario
     *
     * @param id el id del usuario
     * @param id1 el id de la publicacion
     * @return la publicacion
     */
    @GetMapping(path = "usuarios/{id}/publicaciones/{id1}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicacionDto> showPost(@PathVariable("id") Long id,@PathVariable("id1") Long id1){
        try {
            PublicacionDto publicacion = servicioUsuario.recuperaPost(id,id1);
            return ResponseEntity.status(HttpStatus.OK).body(publicacion);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    //  ******************** METODOS POST ********************

    /**
     * Recupera un usuario a partir de su nombre de usuario y contraseña
     *
     * @param nombreUsuario el nombre del usuario
     * @return el usuario encontrado
     */
    @PostMapping(path = "/login/{nombreUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> login(@PathVariable("nombreUsuario") String nombreUsuario,@RequestBody UsuarioDto contra){
        try {
            UsuarioDto usuarioDto = servicioUsuario.iniciaSesion(nombreUsuario,contra);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * crea una nueva publicacion
     *
     * @param id el id del usuario
     * @param nuevaPublicacion la publicacion nueva
     * @return la publicacion creada
     */
    @PostMapping(path = "/usuarios/{id}/publicaciones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicacionDto> addPublicacion(@PathVariable("id") Long id,@RequestBody PublicacionDto nuevaPublicacion){
        try {
            PublicacionDto publicacionDto = servicioUsuario.agregaPublicacion(id,nuevaPublicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(publicacionDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * Crea un usuario nuevo
     *
     * @param nuevoUsuario el usuario a crear
     * @return el usuario
     */
    @PostMapping(path = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> create(@RequestBody UsuarioDto nuevoUsuario) {
        try {
            UsuarioDto usuarioDto = servicioUsuario.agregaUsuario(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
        } catch(Exception e) {
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    //  ******************** METODOS DELETE ********************

    /**
     * Borra un post de un usuario
     *
     * @param id el id del usuario
     * @param idPost el id de la publicacion
     * @return 204 si la publicacion fue borrada
     */
    @DeleteMapping(path = "usuarios/{id}/publicaciones/{id1}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id,@PathVariable("id1") Long idPost)
    {
        try {
            servicioUsuario.borraPublicacion(id,idPost);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    //  ******************** METODOS PUT ********************

    @PutMapping(path = "usuarios/{id}/publicaciones/{id1}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePost(@PathVariable("id") Long id,@PathVariable("id1") Long idPost,@RequestBody PublicacionDto nuevaPublicacion){
        try {
            servicioUsuario.actualizaPublicacion(id,idPost,nuevaPublicacion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
}