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

@RestController
@RequestMapping("/v1") // Versioning
@Slf4j
public class UsuarioRest {

    @Autowired
    ServicioUsuario servicioUsuario;

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

    @GetMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> findUser(@PathVariable("id") Long id){
        try {
            UsuarioDto usuarioDto = servicioUsuario.muestraUsuario(id);
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

    @PostMapping(path = "/usuarios/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
}