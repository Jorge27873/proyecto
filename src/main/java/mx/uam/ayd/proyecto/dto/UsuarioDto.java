package mx.uam.ayd.proyecto.dto;

import lombok.Data;
import mx.uam.ayd.proyecto.negocio.Modelo.Usuario;

import java.util.List;

@Data
public class UsuarioDto {
    private long idUsuario;
    private String nombreDeUsuario;
    private String descripcion;
    private String correo;
    private String passwrd;
    private List<Long> idsPublicaciones;

    public static UsuarioDto creaUsuario(Usuario usuario){
        UsuarioDto usuarioDto = new UsuarioDto();

        usuarioDto.setIdUsuario(usuario.getIdUsuario());
        usuarioDto.setNombreDeUsuario(usuario.getNombreDeUsuario());
        usuarioDto.setDescripcion(usuario.getDescripcion());
        usuarioDto.setCorreo(usuario.getCorreo());
        usuarioDto.setPasswrd(usuario.getPasswrd());
        usuarioDto.setIdsPublicaciones(usuario.recuperaIds());
        return usuarioDto;
    }
}
