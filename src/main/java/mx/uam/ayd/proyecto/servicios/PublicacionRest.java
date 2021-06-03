package mx.uam.ayd.proyecto.servicios;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.dto.PublicacionDto;
import mx.uam.ayd.proyecto.negocio.ServicioPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1") // Versioning
@Slf4j
public class PublicacionRest {
    @Autowired
    ServicioPublicacion servicioPublicacion;

    @GetMapping(path = "/publicaciones/{locacion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicacionDto>> searchLocation(@PathVariable("locacion") String locacion){
        List<PublicacionDto> publicaciones = servicioPublicacion.encuentraPorUbicacion(locacion);
        return ResponseEntity.status(HttpStatus.OK).body(publicaciones);
    }
}