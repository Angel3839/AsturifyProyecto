package com.Angelvf3839.tarea3dwesangel.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Angelvf3839.tarea3dwesangel.modelo.Evento;
import com.Angelvf3839.tarea3dwesangel.modelo.Reseña;

public interface ReseñaRepository extends JpaRepository<Reseña, Long> {
    List<Reseña> findByEventoId(Long eventoId);
    List<Reseña> findByEvento(Evento evento);
}
