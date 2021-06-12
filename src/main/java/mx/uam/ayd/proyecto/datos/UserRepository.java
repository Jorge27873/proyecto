package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.Modelo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository <User, Long> {
     User findByNombre(String nombre);//select * from user where nombre="";
   
}