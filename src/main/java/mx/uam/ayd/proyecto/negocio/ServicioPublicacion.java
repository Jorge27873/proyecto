package mx.uam.ayd.proyecto.negocio;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.datos.PublicacionRepository;
import mx.uam.ayd.proyecto.dto.PublicacionDto;
import mx.uam.ayd.proyecto.negocio.Modelo.Publicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ServicioPublicacion {
    @Autowired
    PublicacionRepository repositorioPublicacion;

    public List<PublicacionDto> encuentraPorUbicacion(String locacion){
        List<PublicacionDto> publicaciones = new ArrayList<>();
        for (Publicacion publicacion:repositorioPublicacion.findAll()) {
            if (publicacion.getLocacion().contains(locacion)){publicaciones.add(PublicacionDto.creaPublicacionDto(publicacion));}
        }
        return publicaciones;
    }
}