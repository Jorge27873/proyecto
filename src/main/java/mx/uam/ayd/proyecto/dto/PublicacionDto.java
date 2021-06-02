package mx.uam.ayd.proyecto.dto;

import lombok.Data;
import mx.uam.ayd.proyecto.negocio.Modelo.Publicacion;

import javax.persistence.ManyToOne;

@Data
public class PublicacionDto {
    private long idPublicacion;
    private String descripcion;
    private String locacion;
    private long usuario;

    public static PublicacionDto creaPublicacionDto(Publicacion publicacion){
        PublicacionDto publicacionDto = new PublicacionDto();
        publicacionDto.setIdPublicacion(publicacion.getIdPublicacion());
        publicacionDto.setDescripcion(publicacion.getDescripcion());
        publicacionDto.setLocacion(publicacion.getLocacion());
        publicacionDto.setUsuario(publicacion.getUsuario().getIdUsuario());
        return publicacionDto;
    }
}