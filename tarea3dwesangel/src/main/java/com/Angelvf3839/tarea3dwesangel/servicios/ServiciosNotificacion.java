package com.Angelvf3839.tarea3dwesangel.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Angelvf3839.tarea3dwesangel.modelo.Notificacion;
import com.Angelvf3839.tarea3dwesangel.repositorios.NotificacionRepository;

@Service
public class ServiciosNotificacion {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarNotificaciones() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> buscarNotificacionPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> listarNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public Notificacion guardarNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}
