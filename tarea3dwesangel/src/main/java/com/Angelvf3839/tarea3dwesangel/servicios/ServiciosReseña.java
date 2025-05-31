package com.Angelvf3839.tarea3dwesangel.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Angelvf3839.tarea3dwesangel.modelo.Reseña;
import com.Angelvf3839.tarea3dwesangel.repositorios.ReseñaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiciosReseña {

    @Autowired
    private ReseñaRepository reseñaRepository;

    public List<Reseña> listarReseñas() {
        return reseñaRepository.findAll();
    }

    public Optional<Reseña> buscarResenaPorId(Long id) {
        return reseñaRepository.findById(id);
    }

    public List<Reseña> listarReseñasPorEvento(Long eventoId) {
        return reseñaRepository.findByEventoId(eventoId);
    }

    public Reseña guardarReseña(Reseña resena) {
        return reseñaRepository.save(resena);
    }

    public void eliminarReseña(Long id) {
    	reseñaRepository.deleteById(id);
    }
}
