package mx.uam.ayd.proyecto.presentacion.principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
/**
 * 
 * Controlador principal
 * 
 *
 */
@Slf4j
@Controller
public class PrincipalController {
	
	/**
	 * Este método está mapeado a la raíz del sitio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/")
    public String vistaPrincipal(Model model) {
        
			// Redirige a la vista principal
    		return "vistaPrincipal/Principal";
    		
    }
	
	
	//@RequestMapping(value = "/vistaUsuarios" , method = RequestMethod.GET)
    public String vistaUsuarios(
    		@RequestParam(value="id", required=true) long id,
    		Model model) {
		try {
			model.addAttribute("id",id);
		
	} catch(Exception ex) {
		log.info(ex.getMessage());
		
	}
		return "vistaUsuarios/vistaUsuarios";

	}
    /**
	 * Este método está mapeado a la raíz del sitio
	 * 
	 * @param model
	 * @return
	
	@RequestMapping(value = "/")
    public String getAgregarUsuario(Model model) {
        
			// Redirige a la vista principal
    		return "VistaAgregarUs/AgregarUsuario";
    	
    }
	@RequestMapping(value = "/vistaUsuarios")
    public String vistaUsuarios() {
        
			// Redirige a la vista principal
    		return "vistaUsuarios/Principal";
    	
    }
     */
    
}
