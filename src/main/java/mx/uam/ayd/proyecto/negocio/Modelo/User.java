
package mx.uam.ayd.proyecto.negocio.Modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
   @Id
   private long id;
   private String nombre;
   private String clave;
   
    
}
