
package mx.uam.ayd.proyecto;

import mx.uam.ayd.proyecto.servicios.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    
    @Bean
    public static BCryptPasswordEncoder passwordEncoder()
    {
        BCryptPasswordEncoder bcrypt= new BCryptPasswordEncoder();
        return bcrypt;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception
    {
        /*auth.inMemoryAuthentication().withUser("jorge").password("123")
                .roles("USER").and().withUser("admin").password("admin")
                .roles("USER","ADMIN");*/
     auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
        
        
    }
    protected void configure(HttpSecurity http) throws Exception
    {
        //https://www.youtube.com/watch?v=b_SMzt6dEPo&list=PLdJYl6XU45uLBGavjhi0I6Ltqh_6XtpHC&index=25
       
        
         http.csrf().disable().authorizeRequests().antMatchers("/**").hasAnyAuthority("ADMIN")
                .and()
                .authorizeRequests().and().formLogin()
                .usernameParameter("user")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().headers().frameOptions().disable();
        
    }
}
