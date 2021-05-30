package mx.uam.ayd.proyecto.presentacion.agregarUsuario;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.negocio.*;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;

@Controller
@Slf4j
public class AgregarUsuarioController {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioGrupo servicioGrupo;

    /**
     *
     * Método invocado cuando se hace una petición GET a la ruta
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/agregarUsuario", method = RequestMethod.GET)
    public String getAgregarUsuario(Model model) {

        log.info("Iniciando Historia de usuario: Agrega usuario");
        try {

            // Invocación al servicio
            List<Grupo> grupos = servicioGrupo.recuperaGrupos();

            // Agregamos el usuario al modelo que se le pasa a la vista
            model.addAttribute("grupos", grupos);

            // Redirigimos a la vista de éxito
            return "vistaAgregarUsuario/FormaAgregarUsuario";

        } catch (Exception ex) {

            // Agregamos el mensaje de error al modelo
            model.addAttribute("error", ex.getMessage());

            // Redirigimos a la vista de error
            return "vistaAgregarUsuario/AgregarUsuarioError";

        }

    }

    @RequestMapping(value = "/regresarMenu", method = RequestMethod.GET)
    public String regresarMenu(Model model) {

        return "vistaPrincipal/Principal";

    }


}
