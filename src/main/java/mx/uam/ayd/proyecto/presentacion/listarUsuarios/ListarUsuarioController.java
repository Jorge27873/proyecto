package mx.uam.ayd.proyecto.presentacion.listarUsuarios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;


@Controller
@Slf4j
public class ListarUsuarioController {
	
	@Autowired
	private ServicioUsuario servicioUsuario;

	/**
	 * 
	 * Método invocado cuando se hace una petición GET a la ruta
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listarUsuario", method = RequestMethod.GET)
    public String getAgregarUsuario(Model model) {
    	
    		log.info("Iniciando Historia de usuario: listar usuario");
                try {
    			
    			// Invocación al servicio
    			List<Usuario> usuarios = servicioUsuario.recuperaUsuarios();
    			
    			// Agregamos el usuario al modelo que se le pasa a la vista
    			model.addAttribute("usuarios", usuarios);
    			
    			// Redirigimos a la vista de éxito
    			return "vistaListarUsuarios/ListarUsuarios";
    			
    		} catch(Exception ex) {
    			
    			// Agregamos el mensaje de error al modelo
    			model.addAttribute("error",ex.getMessage());
    			
    			// Redirigimos a la vista de error
    			return "vistaAgregarUsuario/AgregarUsuarioError";
    			
    		}
    	
    }
}
