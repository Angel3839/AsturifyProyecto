package com.Angelvf3839.tarea3dwesangel.servicios;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Angelvf3839.tarea3dwesangel.modelo.Evento;
import com.Angelvf3839.tarea3dwesangel.repositorios.EventoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiciosEvento {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> buscarEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public List<Evento> buscarEventosPorTipo(String tipoEvento) {
        return eventoRepository.findByTipoEvento(tipoEvento);
    }

    public List<Evento> buscarEventosPorUbicacion(String ubicacion) {
        return eventoRepository.findByUbicacionContainingIgnoreCase(ubicacion);
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}
