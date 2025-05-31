package com.Angelvf3839.tarea3dwesangel.controladores;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Angelvf3839.tarea3dwesangel.modelo.Evento;
import com.Angelvf3839.tarea3dwesangel.modelo.Notificacion;
import com.Angelvf3839.tarea3dwesangel.modelo.Reserva;
import com.Angelvf3839.tarea3dwesangel.modelo.Reseña;
import com.Angelvf3839.tarea3dwesangel.modelo.Sesion;
import com.Angelvf3839.tarea3dwesangel.modelo.TipoEvento;
import com.Angelvf3839.tarea3dwesangel.modelo.TipoUsuario;
import com.Angelvf3839.tarea3dwesangel.modelo.Usuario;
import com.Angelvf3839.tarea3dwesangel.repositorios.EventoRepository;
import com.Angelvf3839.tarea3dwesangel.repositorios.NotificacionRepository;
import com.Angelvf3839.tarea3dwesangel.repositorios.ReservaRepository;
import com.Angelvf3839.tarea3dwesangel.repositorios.ReseñaRepository;
import com.Angelvf3839.tarea3dwesangel.repositorios.UsuarioRepository;
import com.Angelvf3839.tarea3dwesangel.servicios.Controlador;

import org.springframework.ui.Model;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    
    @Autowired
    @Lazy
    private Controlador controlador;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private ReseñaRepository resenaRepository;
    
    @Autowired
    private NotificacionRepository notificacionRepository;



    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String email,
                                @RequestParam("password") String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Por favor, rellena todos los campos.");
            return "redirect:/index";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/index";
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContrasena().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta.");
            return "redirect:/index";
        }

        // Guardamos los datos en sesión
        Sesion sesion = new Sesion(usuario.getId(), usuario.getNombre(), usuario.getTipoUsuario());
        session.setAttribute("usuario", sesion);
        session.setAttribute("tiempoInicio", System.currentTimeMillis());

        return "redirect:/inicio";
    }

    

    @GetMapping("/inicio")
    public String mostrarInicio() {
        return "inicio";  // nombre del archivo HTML: inicio.html
    }
    
    @GetMapping("/contacto")
    public String mostrarContacto(Model model) {
        return "contacto";
    }

    @GetMapping({"/", "/index"})
    public String mostrarIndex(Model model) {
        return "index";
    }

    @GetMapping("/crearEvento")
    public String mostrarCrearEvento(Model model) {
        return "crearEvento";
    }
    
    @GetMapping("/conocenos")
    public String mostrarConocenos(Model model) {
        return "conocenos";
    }
    
    @GetMapping("/eventos")
    public String mostrarEventos(Model model) {
        List<Evento> todosLosEventos = eventoRepository.findAll();

        // Filtrar eventos cuya fecha aún no ha pasado
        LocalDate hoy = LocalDate.now();
        List<Evento> eventosFuturos = todosLosEventos.stream()
            .filter(e -> !e.getFecha().isBefore(hoy)) // incluir hoy o fechas futuras
            .collect(Collectors.toList());

        model.addAttribute("eventos", eventosFuturos);
        return "eventos";
    }

    
    @PostMapping("/eventos/crear")
    public String crearEvento(@RequestParam(value = "titulo", required = false) String titulo,
                              @RequestParam(value = "descripcion", required = false) String descripcion,
                              @RequestParam(value = "ubicacion", required = false) String ubicacion,
                              @RequestParam(value = "fecha", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                              @RequestParam(value = "hora", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
                              @RequestParam(value = "tipo", required = false) TipoEvento tipo,
                              @RequestParam(value = "plazas", required = false) Integer plazas,
                              @RequestParam(value = "precio", required = false) Double precio,
                              RedirectAttributes redirectAttributes) {
    	
        if ((titulo == null || titulo.isBlank()) &&
                (descripcion == null || descripcion.isBlank()) &&
                (ubicacion == null || ubicacion.isBlank()) &&
                fecha == null &&
                hora == null &&
                tipo == null &&
                plazas == null &&
                precio == null) {

                redirectAttributes.addFlashAttribute("error", "Por favor, completa todos los campos antes de enviar el formulario.");
                return "redirect:/crearEvento";
            }

    	
    	if (titulo == null || titulo.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El título no puede estar vacío.");
            return "redirect:/crearEvento";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La descripción no puede estar vacía.");
            return "redirect:/crearEvento";
        }

        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La ubicación no puede estar vacía.");
            return "redirect:/crearEvento";
        }

        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "La fecha debe ser válida y no anterior a hoy.");
            return "redirect:/crearEvento";
        }

        if (hora == null) {
            redirectAttributes.addFlashAttribute("error", "La hora no puede estar vacía.");
            return "redirect:/crearEvento";
        }

        if (tipo == null) {
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar un tipo de evento.");
            return "redirect:/crearEvento";
        }

        if (plazas <= 0) {
            redirectAttributes.addFlashAttribute("error", "El número de plazas debe ser mayor que 0.");
            return "redirect:/crearEvento";
        }

        if (precio < 0) {
            redirectAttributes.addFlashAttribute("error", "El precio no puede ser negativo.");
            return "redirect:/crearEvento";
        }
    	
    	try {
            System.out.println("Intentando crear evento con los siguientes datos:");
            System.out.println("Título: " + titulo);
            System.out.println("Descripción: " + descripcion);
            System.out.println("Ubicación: " + ubicacion);
            System.out.println("Fecha: " + fecha);
            System.out.println("Hora: " + hora);
            System.out.println("Tipo: " + tipo);
            System.out.println("Plazas: " + plazas);
            System.out.println("Precio: " + precio);

            Evento evento = new Evento();
            evento.setTitulo(titulo);
            evento.setDescripcion(descripcion);
            evento.setUbicacion(ubicacion);
            evento.setFecha(fecha);
            evento.setHora(hora);
            evento.setTipoEvento(tipo);
            evento.setCapacidad(plazas);
            evento.setPrecio(precio);

            eventoRepository.save(evento);

            System.out.println("Evento guardado correctamente en la base de datos.");

            // Añadir notificación a todos los usuarios
            List<Usuario> usuarios = usuarioRepository.findAll();
            for (Usuario usuario : usuarios) {
                Notificacion notificacion = new Notificacion();
                notificacion.setUsuario(usuario);
                notificacion.setMensaje("Nuevo Evento: " + evento.getTitulo());
                notificacion.setFechaEnvio(LocalDateTime.now());
                notificacionRepository.save(notificacion);
            }

            redirectAttributes.addFlashAttribute("mensaje", "Evento creado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al crear el evento: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al crear el evento.");
        }

        return "redirect:/crearEvento";
    }



    @PostMapping("/reservar")
    public String reservarEvento(@RequestParam("eventoId") Long eventoId,
                                 @RequestParam("plazas") int cantidadPlazas,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para reservar.");
            return "redirect:/index";
        }

        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        if (eventoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El evento no existe.");
            return "redirect:/eventos";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(sesion.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no válido.");
            return "redirect:/eventos";
        }

        Evento evento = eventoOpt.get();
        
        if (evento.getFecha().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "No puedes reservar eventos que ya han pasado.");
            return "redirect:/eventos";
        }


        if (cantidadPlazas > evento.getCapacidad()) {
            redirectAttributes.addFlashAttribute("error", "No hay suficientes plazas disponibles.");
            return "redirect:/eventos";
        }

        Usuario usuario = usuarioOpt.get();

        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setUsuario(usuario);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setCantidadPlazas(cantidadPlazas);

        evento.setCapacidad(evento.getCapacidad() - cantidadPlazas);
        eventoRepository.save(evento);
        reservaRepository.save(reserva);

        // ✅ Formatear fecha y hora del evento
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        String fechaFormateada = evento.getFecha().format(formatterFecha);
        String horaFormateada = evento.getHora().format(formatterHora);

        // ✅ Obtener y formatear fecha y hora actual (momento de la reserva)
        LocalDateTime ahora = LocalDateTime.now();
        String fechaActual = ahora.format(formatterFecha);
        String horaActual = ahora.format(formatterHora);

        // Crear notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setFechaEnvio(ahora);
        notificacion.setMensaje("Has reservado " + cantidadPlazas + " plaza(s) el " + fechaActual + " a las " + horaActual +
                                 " para " + evento.getTitulo() + " el " + fechaFormateada + " a las " + horaFormateada + ".");
        notificacionRepository.save(notificacion);

        redirectAttributes.addFlashAttribute("mensaje", "Reserva realizada con éxito.");
        return "redirect:/eventos";
    }






    @PostMapping("/resenas")
    public String publicarResena(@RequestParam("eventoId") Long eventoId,
                                 @RequestParam("comentario") String comentario,
                                 @RequestParam("calificacion") int calificacion,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para publicar una reseña.");
            return "redirect:/index";
        }

        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(sesion.getIdUsuario());

        if (eventoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Evento o usuario no encontrado.");
            return "redirect:/eventos";
        }

        Reseña resena = new Reseña();
        resena.setEvento(eventoOpt.get());
        resena.setUsuario(usuarioOpt.get());
        resena.setComentario(comentario);
        resena.setCalificacion(calificacion);
        resena.setFechaResena(LocalDate.now());

        resenaRepository.save(resena);

        redirectAttributes.addFlashAttribute("mensaje", "¡Gracias por tu reseña!");
        return "redirect:/resenas?eventoId=" + eventoId;
    }



    @GetMapping("/resenas")
    public String mostrarResenas(@RequestParam("eventoId") Long eventoId, Model model) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        if (eventoOpt.isEmpty()) {
            return "redirect:/eventos"; // o mostrar un error
        }

        Evento evento = eventoOpt.get();
        List<Reseña> resenas = resenaRepository.findByEventoId(eventoId);

        model.addAttribute("evento", evento); // esto es necesario para el formulario
        model.addAttribute("resenas", resenas);
        return "resenas";
    }



    @GetMapping("/notificaciones")
    public String mostrarNotificaciones(Model model, HttpSession session) {
        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            return "redirect:/index";
        }

        List<Evento> eventosDisponibles = eventoRepository.findAll();
        List<Reserva> reservasUsuario = reservaRepository.findByUsuarioId(sesion.getIdUsuario());

        // NUEVO: Obtener notificaciones del usuario
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(sesion.getIdUsuario());

        model.addAttribute("eventosNotificaciones", eventosDisponibles);
        model.addAttribute("reservasNotificaciones", reservasUsuario);
        model.addAttribute("notificaciones", notificaciones); // añadir al modelo

        return "notificaciones";
    }



    @GetMapping("/perfilUsuario")
    public String mostrarPerfilUsuario(Model model, HttpSession session) {
        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(sesion.getIdUsuario());
            usuario.ifPresent(u -> model.addAttribute("usuario", u));
        }
        return "perfilUsuario";
    }

    @GetMapping("/editarUsuario")
    public String mostrarFormularioEdicion(HttpSession session, Model model) {
        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            return "redirect:/index";
        }

        Optional<Usuario> usuario = usuarioRepository.findById(sesion.getIdUsuario());
        usuario.ifPresent(u -> model.addAttribute("usuario", u));

        return "editarUsuario";
    }

//    @GetMapping("/reservas")
//    public String mostrarReservas(HttpSession session, Model model) {
//        Sesion sesion = (Sesion) session.getAttribute("usuario");
//        if (sesion == null) {
//            return "redirect:/index";
//        }
//
//        Optional<Usuario> usuarioOpt = usuarioRepository.findById(sesion.getIdUsuario());
//        if (usuarioOpt.isEmpty()) {
//            return "redirect:/index";
//        }
//
//        Usuario usuario = usuarioOpt.get();
//
//        List<Reserva> reservas = reservaRepository.findByUsuario(usuario);
//        model.addAttribute("reservas", reservas);
//
//        return "reservas";
//    }


    @GetMapping("/reservas")
    public String verReservas(HttpSession session, Model model) {
        Sesion sesion = (Sesion) session.getAttribute("usuario");

        if (sesion == null) {
            return "redirect:/index";
        }

        List<Reserva> reservasUsuario = reservaRepository.findByUsuarioId(sesion.getIdUsuario());
        model.addAttribute("reservas", reservasUsuario);

        return "reservas";
    }


    @PostMapping("/actualizarUsuario")
    public String actualizarUsuario(@RequestParam("nombre") String nombre,
                                    @RequestParam("nuevaContrasena") String contrasena,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/index";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre no puede estar vacío.");
            return "redirect:/editarUsuario";
        }

        if (contrasena == null || contrasena.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            return "redirect:/editarUsuario";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(sesion.getIdUsuario());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(nombre);
            usuario.setContrasena(contrasena);
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Datos actualizados correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }

        return "redirect:/editarUsuario";
    }



    @PostMapping("/eliminarResena")
    public String eliminarResena(@RequestParam Long resenaId,
                                 @RequestParam String motivo,
                                 RedirectAttributes redirectAttributes) {
        resenaRepository.deleteById(resenaId);
        redirectAttributes.addFlashAttribute("mensaje", "Reseña eliminada correctamente.");
        return "redirect:/gestionResenas";
    }

    @GetMapping("/gestionResenas")
    public String gestionarResenas(Model model, HttpSession session) {
        Sesion sesion = (Sesion) session.getAttribute("usuario");

        if (sesion == null || !sesion.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
            return "redirect:/403";
        }

        List<Reseña> reseñas = resenaRepository.findAll();
        model.addAttribute("reseñas", reseñas);
        return "gestionResenas";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam("nombre") String nombre,
                                   @RequestParam("email") String email,
                                   @RequestParam("contrasena") String contrasena,
                                   @RequestParam("confirmarContrasena") String confirmarContrasena,
                                   RedirectAttributes redirectAttributes) {

        // Validaciones de campos vacíos
        if (nombre == null || nombre.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty() ||
            confirmarContrasena == null || confirmarContrasena.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("error", "Por favor, completa todos los campos para poder registrarte.");
            return "redirect:/index";
        }

        // Validación de formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            redirectAttributes.addFlashAttribute("error", "El formato del correo electrónico no es válido.");
            return "redirect:/index";
        }


        // Validación de contraseña segura
        if (contrasena.length() < 6 || !contrasena.matches(".*[A-Z].*") || !contrasena.matches(".*\\d.*")) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número.");
            return "redirect:/index";
        }

        // Validación de coincidencia de contraseñas
        if (!contrasena.equals(confirmarContrasena)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/index";
        }

        // Validación de existencia de email
        Optional<Usuario> existente = usuarioRepository.findByEmail(email);
        if (existente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/index";
        }

        // Registro de usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContrasena(contrasena);
        nuevoUsuario.setTipoUsuario(TipoUsuario.REGISTRADO);

        usuarioRepository.save(nuevoUsuario);

        redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/index";
    }


    @GetMapping("/403")
    public String accesoDenegado() {
        return "403";
    }


    @GetMapping("/eventosFiltrar")
    public String mostrarEventosFiltrados(
            @RequestParam(value = "tipo", required = false) TipoEvento tipo,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "precioMin", required = false) Double precioMin,
            @RequestParam(value = "precioMax", required = false) Double precioMax,
            Model model) {

        List<Evento> eventos = eventoRepository.findAll();

        if (tipo != null) {
            eventos = eventos.stream()
                    .filter(e -> e.getTipoEvento() == tipo)
                    .collect(Collectors.toList());
        }

        if (fecha != null) {
            eventos = eventos.stream()
                    .filter(e -> e.getFecha().equals(fecha))
                    .collect(Collectors.toList());
        }

        if (precioMin != null) {
            eventos = eventos.stream()
                    .filter(e -> e.getPrecio() >= precioMin)
                    .collect(Collectors.toList());
        }

        if (precioMax != null) {
            eventos = eventos.stream()
                    .filter(e -> e.getPrecio() <= precioMax)
                    .collect(Collectors.toList());
        }

        model.addAttribute("eventos", eventos);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("fechaSeleccionada", fecha);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);

        return "eventos";
    }
    
    @PostMapping("/cancelarReserva")
    public String cancelarReserva(@RequestParam Long reservaId, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Reserva no encontrada.");
            return "redirect:/reservas";
        }

        Reserva reserva = reservaOpt.get();
        Evento evento = reserva.getEvento();

        // Devolver plazas al evento
        evento.setCapacidad(evento.getCapacidad() + reserva.getCantidadPlazas());
        eventoRepository.save(evento);

        // Eliminar la reserva
        reservaRepository.deleteById(reservaId);

        redirectAttributes.addFlashAttribute("mensaje", "Reserva cancelada correctamente.");
        return "redirect:/reservas";
    }

    @PostMapping("/eliminarNotificacion")
    public String eliminarNotificacion(@RequestParam("notificacionId") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Verificamos el usuario en sesión
        Sesion sesion = (Sesion) session.getAttribute("usuario");
        if (sesion == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/index";
        }

        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(id);

        if (notificacionOpt.isPresent()) {
            Notificacion notificacion = notificacionOpt.get();

            // Validamos que la notificación pertenezca al usuario
            if (notificacion.getUsuario().getId().equals(sesion.getIdUsuario())) {
                notificacionRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("mensaje", "Notificación eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar esta notificación.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la notificación.");
        }

        return "redirect:/notificaciones";
    }








    

}