package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.datos.GrupoRepository;
import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;
import mx.uam.ayd.proyecto.dto.UsuarioDto;

@Slf4j
@Service
public class ServicioUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    /**
     * Permite agregar un usuario
     *
     * @param usuarioDto
     * @return dto con el id del usuario
     */
    @Transactional// cuando un metodo no es transaccional y el fethctype de la entidad es lazy y recupero un grupo, solo
    //recupero al grupo no a sus usuarios, cuando se marca como transaccional y recuepro a un objeto y este tiene 
    //dependencias del tipo lazy mientras estoy dentro de la transaccion y trato de ir hacia sus 
    //dependencias el si es capaz de recuperarlas
    public UsuarioDto agregaUsuario(UsuarioDto usuarioDto) {
        // Regla de negocio: No se permite agregar dos usuarios con el mismo nombre y apellido
        Usuario usuario = usuarioRepository.findByNombreAndApellido(usuarioDto.getNombre(), usuarioDto.getApellido());
        if (usuario != null) {
            throw new IllegalArgumentException("Ese usuario ya existe");
        }
        Optional<Grupo> optGrupo = grupoRepository.findById(usuarioDto.getGrupo());
        if (!optGrupo.isPresent()) {
            throw new IllegalArgumentException("No se encontró el grupo");
        }
        Grupo grupo = optGrupo.get();
        usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setGrupo(grupo);
        usuario.setEdad(usuarioDto.getEdad());
        usuarioRepository.save(usuario);
        usuarioDto.setIdUsuario(usuario.getIdUsuario());
        grupo.addUsuario(usuario);
        grupoRepository.save(grupo);
        log.info("Agregando usuario nombre: " + usuarioDto.getNombre() + " id:" + usuarioDto.getIdUsuario());
        return usuarioDto;
    }

    /**
     * Recupera todos los usuarios existentes
     *
     * @return Una lista con los usuarios (o lista vacía)
     */
    public List<UsuarioDto> recuperaUsuarios() {

        System.out.println("usuarioRepository = " + usuarioRepository);

        List<UsuarioDto> usuarios = new ArrayList<>();

        for (Usuario usuario : usuarioRepository.findAll()) {
            usuarios.add(UsuarioDto.creaDto(usuario));
        }

        return usuarios;
    }

    /**
     * Recupera un usuario en base a su id
     *
     * @return un usuario (o null)
     */
    public UsuarioDto recuperaUsuario(long id) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (!usuario.isPresent()) {
            throw new IllegalArgumentException("No se encontró el usuario");
        }
        Usuario user = usuario.get();
        UsuarioDto usuariodto = UsuarioDto.creaDto(user);
        return usuariodto;
    }

    /**
     * Elimina un usuario en base a su id
     *
     *
     */
    public void eliminaUsuario(long id) {

        usuarioRepository.deleteById(id);

    }

    /**
     * Actualiza un usuario en base a su id
     *
     *
     */
    public UsuarioDto actualizaUsuario(Long id, UsuarioDto usuarioDto) {
        // Regla de negocio: No se permite agregar dos usuarios con el mismo nombre y apellido
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (!usuario.isPresent()) {
            throw new IllegalArgumentException("No se encontró el usuario");
        }
        Optional<Grupo> optGrupo = grupoRepository.findById(usuarioDto.getGrupo());
        if (!optGrupo.isPresent()) {
            throw new IllegalArgumentException("No se encontró el grupo");
        }
        Grupo grupo = optGrupo.get();//grupo del dto  (NUEVO)
        Usuario user = usuario.get();
        user.setNombre(usuarioDto.getNombre());
        user.setApellido(usuarioDto.getApellido());
        log.info("grupo " + grupo.getIdGrupo() + " user grupo " + user.getGrupo().getIdGrupo());
        if (grupo.getIdGrupo() != user.getGrupo().getIdGrupo()) {
            grupo.addUsuario(user);
            user.getGrupo().deleteUsuario(user);
            grupoRepository.save(user.getGrupo());
        }
        user.setGrupo(grupo);
        user.setEdad(usuarioDto.getEdad());
        usuarioRepository.save(user);
        grupoRepository.save(grupo);
        //verificamos que usuarios hay en cada grupo:
        Optional<Grupo> g = grupoRepository.findById((long) 1);
        Grupo g1 = g.get();
        for (int i = 0; i < g1.getUsuarios().size(); i++) {
            log.info("grupo 1:" + g1.getUsuarios().get(i).getIdUsuario());
        }
        log.info("empieza grupo 2");
        Optional<Grupo> g2 = grupoRepository.findById((long) 2);
        Grupo g12 = g2.get();
        for (int i = 0; i < g12.getUsuarios().size(); i++) {
            log.info("grupo 2:" + g12.getUsuarios().get(i).getIdUsuario());
        }
        log.info("Actualizando usuario nombre: " + usuarioDto.getNombre() + " id:" + id);
        return usuarioDto.creaDto(user);
    }

    public Usuario agregaUsuario(String nombre, String apellido, String nombreGrupo) {

        // Regla de negocio: No se permite agregar dos usuarios con el mismo nombre y apellido
        Usuario usuario = usuarioRepository.findByNombreAndApellido(nombre, apellido);

        if (usuario != null) {
            throw new IllegalArgumentException("Ese usuario ya existe");
        }

        Grupo grupo = grupoRepository.findByNombre(nombreGrupo);

        if (grupo == null) {
            throw new IllegalArgumentException("No se encontró el grupo");
        }

        log.info("Agregando usuario nombre: " + nombre + " apellido:" + apellido);

        usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        usuarioRepository.save(usuario);

        grupo.addUsuario(usuario);

        grupoRepository.save(grupo);

        return usuario;

    }

    /**
     * Recupera todos los usuarios existentes
     *
     * @return Una lista con los usuarios (o lista vacía)
     */
    public List<Usuario> recuperaUsuariosnoDto() {

        System.out.println("usuarioRepository = " + usuarioRepository);

        List<Usuario> usuarios = new ArrayList<>();

        for (Usuario usuario : usuarioRepository.findAll()) {
            usuarios.add(usuario);
        }

        return usuarios;
    }
}
