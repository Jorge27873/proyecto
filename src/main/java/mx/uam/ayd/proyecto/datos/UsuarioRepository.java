package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.uam.ayd.proyecto.negocio.Modelo.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository <Usuario, Long> {

    Usuario findBynombreDeUsuario(String nombre);
    Usuario findByCorreo(String correo);
}