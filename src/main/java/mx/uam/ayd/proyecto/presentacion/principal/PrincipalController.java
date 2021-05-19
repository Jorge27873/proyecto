package mx.uam.ayd.proyecto.presentacion.principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * Controlador principal
 * 
 *
 */
@Controller
public class PrincipalController {
	
	/**
	 * Este m�todo est� mapeado a la ra�z del sitio
	 * 
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/")
    public String getAgregarUsuario(Model model) {
			// Redirige a la vista principal
    		return "vistaPrincipal/Principal";
    	
    }


}
