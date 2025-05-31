package com.Angelvf3839.tarea3dwesangel.repositorios;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Angelvf3839.tarea3dwesangel.modelo.Evento;
import com.Angelvf3839.tarea3dwesangel.modelo.TipoEvento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByTipoEvento(String tipoEvento);
    List<Evento> findByUbicacionContainingIgnoreCase(String ubicacion);
    List<Evento> findByTipoEvento(TipoEvento tipoEvento);

}
