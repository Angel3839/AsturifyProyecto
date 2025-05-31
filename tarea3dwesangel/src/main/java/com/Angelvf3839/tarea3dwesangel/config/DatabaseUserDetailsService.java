package com.Angelvf3839.tarea3dwesangel.config;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.Angelvf3839.tarea3dwesangel.modelo.Sesion;
import com.Angelvf3839.tarea3dwesangel.modelo.Usuario;
import com.Angelvf3839.tarea3dwesangel.repositorios.UsuarioRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpServletRequest request;

    @Autowired
    public DatabaseUserDetailsService(UsuarioRepository usuarioRepository, HttpServletRequest request) {
        this.usuarioRepository = usuarioRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Guardar información en sesión HTTP
        HttpSession session = request.getSession();
        session.setAttribute("usuario", new Sesion(usuario.getId(), usuario.getNombre(), usuario.getTipoUsuario()));
        session.setAttribute("tiempoInicio", System.currentTimeMillis());

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasena())
                .roles(usuario.getTipoUsuario().name())
                .build();
    }
}
