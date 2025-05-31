package com.Angelvf3839.tarea3dwesangel.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.Angelvf3839.tarea3dwesangel.modelo.Sesion;
import com.Angelvf3839.tarea3dwesangel.modelo.TipoUsuario;
import com.Angelvf3839.tarea3dwesangel.modelo.Usuario;
import com.Angelvf3839.tarea3dwesangel.repositorios.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers("/imagenes/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/", "/index", "/registro").permitAll()

                // Accesible por todos los roles
                .requestMatchers("/inicio").hasAnyRole("ADMINISTRADOR", "REGISTRADO", "INVITADO")

                // Solo ADMINISTRADOR
                .requestMatchers("/eventos/crear", "/crearEvento", "/gestionReseñas").hasRole("ADMINISTRADOR")

                // Solo REGISTRADO
                .requestMatchers("/menuUsuario/**").hasRole("REGISTRADO")

                // Compartido entre ADMINISTRADOR y REGISTRADO
                .requestMatchers("/notificaciones", "/notificaciones/**", "/reservas", "/reservas/**", "/editarUsuario").hasAnyRole("ADMINISTRADOR", "REGISTRADO")

                // Solo INVITADO
                .requestMatchers("/invitado/**").hasRole("INVITADO")

                // Cualquier otra petición
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/index")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/inicio", true)
                .failureUrl("/index?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(e -> e
                .accessDeniedPage("/403")
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/index?expired=true")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Usa BCrypt en producción
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
