package mx.uam.ayd.proyecto.presentacion.listarUsuarios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.dto.UsuarioDto;
import mx.uam.ayd.proyecto.negocio.ServicioGrupo;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ListarUsuarioController {

    @Autowired
    private ServicioUsuario servicioUsuario;
     @Autowired
    private ServicioGrupo servicioGrupo;

    
    @RequestMapping(value = "/listarUsuario", method = RequestMethod.GET)
    public String getAgregarUsuario() {

        return "vistaListarUsuarios/ListarUsuarios";

    }
    
    @RequestMapping(value = "/actualizarUsuario", method = RequestMethod.POST)
    public String actualizar(@RequestParam(value="id", required=true)Long id,
    		Model model) {
        UsuarioDto user=servicioUsuario.recuperaUsuario(id);
        // Agregamos el usuario al modelo que se le pasa a la vista
    	model.addAttribute("nombre", user.getNombre());
    	model.addAttribute("apellido", user.getApellido());
        model.addAttribute("edad", user.getEdad());
        Grupo g=servicioGrupo.recuperaGrupo(user.getGrupo());
        model.addAttribute("grupo",g.getNombre());
        List<Grupo> grupos = servicioGrupo.recuperaGrupos();
        model.addAttribute("grupos", grupos);
        model.addAttribute("id", id);
        return "vistaEditarUsuario/FormaEditarUsuario";

    }
}
