package mx.uam.ayd.proyecto.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.dto.UsuarioDto;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1") // Versionamiento
@Slf4j //para usar la consola y generar trazas
public class UsuarioRestController {

    @Autowired
    private ServicioUsuario servicioUsuarios;

    /**
     * Permite recuperar todos los usuarios
     *
     * @return
     */
    @GetMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioDto>> retrieveAll() {

        List<UsuarioDto> usuarios = servicioUsuarios.recuperaUsuarios();

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);

    }

    /**
     * Método que permite agregar un usuario
     *
     * @param nuevoUsuario
     * @return
     */
    @PostMapping(path = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> create(@RequestBody UsuarioDto nuevoUsuario) {

        try {
            UsuarioDto usuarioDto = servicioUsuarios.agregaUsuario(nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);

        } catch (Exception ex) {

            HttpStatus status;

            if (ex instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            throw new ResponseStatusException(status, ex.getMessage());
        }

    }

    /**
     * Permite recuperar un usuario a partir de su ID
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> retrieve(@PathVariable("id") Long id) {
        UsuarioDto usuario = servicioUsuarios.recuperaUsuario(id);
        log.info("recuperando usuario con id " + usuario.getIdUsuario());
        return ResponseEntity.status(HttpStatus.OK).body(usuario);

    }

    @DeleteMapping(path = "/usuarios/{id}")//convencion: en el modelo de dominio de llaman en singular las entidades
    //y a la hora de hacer peticiones en las URIS se ponen los nombres de las entidades en plural
    //URI es un identificador de recursos ej http://localhost:8080/v1/usuarios/1
    //URL ej http://localhost:8080
    //cuando tenemos relaciones de composicion en el modelo de dominio, las rutas se escriben: /clientes/{id}/compras/{id}/compraProducto
    //cuando tenemos relaciones de asociacion en el modelo de dominio, las rutas se escriben: /producto
    public ResponseEntity<Long> deleteEmployee(@PathVariable(value = "id") Long id) {
        UsuarioDto employee = servicioUsuarios.recuperaUsuario(id);
        if (employee != null) {
            servicioUsuarios.eliminaUsuario(id);
            log.info("eliminando usuario con id " + id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no se encontro el usuario con id " + id);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(path = "/usuarios/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable(value = "id") Long id, @RequestBody UsuarioDto usuario) {

        try {
            UsuarioDto usuarioDto = servicioUsuarios.actualizaUsuario(id, usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);

        } catch (Exception ex) {

            HttpStatus status;

            if (ex instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            throw new ResponseStatusException(status, ex.getMessage());
        }

    }

}
