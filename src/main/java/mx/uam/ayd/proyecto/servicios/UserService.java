
package mx.uam.ayd.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import mx.uam.ayd.proyecto.datos.UserRepository;
import mx.uam.ayd.proyecto.negocio.Modelo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepo;
    
    
    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        User us= userRepo.findByNombre(string);
        List<GrantedAuthority> roles= new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDet= new org.springframework.security.core.userdetails.User(us.getNombre(),us.getClave(),roles);
       return userDet;
    }
    
}
