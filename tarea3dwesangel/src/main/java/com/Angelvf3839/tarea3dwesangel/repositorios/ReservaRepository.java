package com.Angelvf3839.tarea3dwesangel.repositorios;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Angelvf3839.tarea3dwesangel.modelo.Reserva;
import com.Angelvf3839.tarea3dwesangel.modelo.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByEventoId(Long eventoId);
    List<Reserva> findByUsuario(Usuario usuario);

}
