package com.Angelvf3839.tarea3dwesangel.repositorios;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Angelvf3839.tarea3dwesangel.modelo.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);

}
