package com.Angelvf3839.tarea3dwesangel.repositorios;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Angelvf3839.tarea3dwesangel.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNombre(String nombre);
    
    boolean existsByEmail(String email);

    
}
