package mx.uam.ayd.proyecto;

import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import mx.uam.ayd.proyecto.config.FileStorageProperties;
import mx.uam.ayd.proyecto.datos.UserRepository;
import mx.uam.ayd.proyecto.negocio.Modelo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ProyectoApplication {
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    BCryptPasswordEncoder encoder;
    
    public static void main(String[] args) {
        SpringApplication.run(ProyectoApplication.class, args);
    }
    
    @PostConstruct
	public void inicia() {
		
		inicializaBD();
		
		//controlPrincipal.inicia();
	}
	
	
	/**
	 * 
	 * Inicializa la BD con datos
	 * 
	 * 
	 */
	public void inicializaBD() {
		
        User us= new User();
        us.setId(1);
        us.setNombre("jorge");
        us.setClave(encoder.encode("123"));
        userRepo.save(us);
				
	}
}