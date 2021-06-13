package mx.uam.ayd.proyecto.servicios;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.dto.PublicacionDto;
import mx.uam.ayd.proyecto.dto.UsuarioDto;
import mx.uam.ayd.proyecto.negocio.Modelo.Usuario;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mx.uam.ayd.proyecto.utils.AppConstants;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;


@PreAuthorize("authenticated")
@RestController
@RequestMapping("/v1") // Versioning
@Slf4j
public class UsuarioRest {
   
	ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	FileStorageService fileStorageService;

    @Autowired
    ServicioUsuario servicioUsuario;
    
    
    //  ******************** METODOS GET ********************

    
    /*
    
    
    
    Los endpoints son las URLs de un API o un backend que responden a una petición.
    
    
    
    
    @RequestMapping(value = AppConstants.EMPLOYEE_URI, method = RequestMethod.GET)
	public List<Employee> getAllEmployees() {
		return applicationService.getAllEmployees();
	}
*/
	@RequestMapping(value = AppConstants.DOWNLOAD_URI, method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		Resource resource = fileStorageService.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (contentType == null) {
			contentType = AppConstants.DEFAULT_CONTENT_TYPE;
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						String.format(AppConstants.FILE_DOWNLOAD_HTTP_HEADER, resource.getFilename()))
				.body(resource);
	}
    

    
    
    @GetMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> findUserById(@PathVariable("id") long id){
        try {
            UsuarioDto usuarioDto = servicioUsuario.recuperaUsuario(id);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * Recupera todas la publicaciones de un usuario
     *
     * @param id el id del usuario
     * @return las publicaciones del usuario
     */
    @GetMapping(path = "usuarios/{id}/publicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicacionDto>> showPosts(@PathVariable("id") Long id){
        try {
            List <PublicacionDto> publicaciones = servicioUsuario.recuperaPosts(id);
            return ResponseEntity.status(HttpStatus.OK).body(publicaciones);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * Recupera un post especifico de un usuario
     *
     * @param id el id del usuario
     * @param id1 el id de la publicacion
     * @return la publicacion
     */
    @GetMapping(path = "usuarios/{id}/publicaciones/{id1}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicacionDto> showPost(@PathVariable("id") Long id,@PathVariable("id1") Long id1){
        try {
            PublicacionDto publicacion = servicioUsuario.recuperaPost(id,id1);
            return ResponseEntity.status(HttpStatus.OK).body(publicacion);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @GetMapping(value = "search/{nombre}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioDto>> search(@PathVariable String nombre){
        List<UsuarioDto> usuarios = servicioUsuario.buscar(nombre);
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    //  ******************** METODOS POST ********************

    /**
     * Recupera un usuario a partir de su nombre de usuario y contraseña
     *
     * @param nombreUsuario el nombre del usuario
     * @return el usuario encontrado
     */
    @PostMapping(path = "/login/{nombreUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> login(@PathVariable("nombreUsuario") String nombreUsuario,@RequestBody UsuarioDto contra){
        try {
            UsuarioDto usuarioDto = servicioUsuario.iniciaSesion(nombreUsuario,contra);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    /**
     * crea una nueva publicacion
     *
     * @param id el id del usuario
     * @param jsondata la publicacion nueva
     * @return la publicacion creada
     */
    @PostMapping(path = "/usuarios/{id}/publicaciones", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicacionDto> addPublicacion(@PathVariable("id") Long id,@RequestParam(required=true, value="file") MultipartFile file,@RequestParam(required=true, value="jsondata")String jsondata) throws IOException{
        
        String fileName = fileStorageService.storeFile(file);
	String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppConstants.DOWNLOAD_PATH)
		.path(fileName).toUriString();
        PublicacionDto nuevaPublicacionDto = objectMapper.readValue(jsondata, PublicacionDto.class);
        nuevaPublicacionDto.setImagen(fileDownloadUri);
        
        try {
            PublicacionDto publicacionDto = servicioUsuario.agregaPublicacion(id,nuevaPublicacionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(publicacionDto);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
    
    /**
     * Crea un usuario nuevo
     *
     * @param jsondata el usuario a crear
     * @return el usuario
     */
    @PostMapping(path = "/usuarios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> create(@RequestParam(required=true, value="file") MultipartFile file,@RequestParam(required=true, value="jsondata")String jsondata) throws IOException {
       
        String fileName = fileStorageService.storeFile(file);
	String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppConstants.DOWNLOAD_PATH)
		.path(fileName).toUriString();
        UsuarioDto userData = objectMapper.readValue(jsondata, UsuarioDto.class);
        userData.setImagen(fileDownloadUri);
        
        
        try {
            UsuarioDto usuarioDto = servicioUsuario.agregaUsuario(userData);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
        } catch(Exception e) {
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    //  ******************** METODOS DELETE ********************

    /**
     * Borra un post de un usuario
     *
     * @param id el id del usuario
     * @param idPost el id de la publicacion
     * @return 204 si la publicacion fue borrada
     */
    @DeleteMapping(path = "usuarios/{id}/publicaciones/{id1}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id,@PathVariable("id1") Long idPost)
    {
        try {
            servicioUsuario.borraPublicacion(id,idPost);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    //  ******************** METODOS PUT ********************

    @PutMapping(path = "usuarios/{id}/publicaciones/{id1}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePost(@PathVariable("id") Long id,@PathVariable("id1") Long idPost,@RequestBody PublicacionDto nuevaPublicacion){
        try {
            servicioUsuario.actualizaPublicacion(id,idPost,nuevaPublicacion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            HttpStatus status;
            if(e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
}