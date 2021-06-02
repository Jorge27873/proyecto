package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.Modelo.Publicacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends CrudRepository <Publicacion, Long> {
}
