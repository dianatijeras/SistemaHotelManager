package co.edu.uniquindio.sistemahotelmanager.service;

import co.edu.uniquindio.sistemahotelmanager.data.DataStore;
import co.edu.uniquindio.sistemahotelmanager.dto.ReservaRequestDTO;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoReserva;
import co.edu.uniquindio.sistemahotelmanager.model.Habitacion;
import co.edu.uniquindio.sistemahotelmanager.model.Huesped;
import co.edu.uniquindio.sistemahotelmanager.model.Reserva;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la lógica de negocio relacionada con las reservas en el sistema de gestión hotelera.
 */
@Service
public class ReservaService {

    private final DataStore dataStore;
    private final HabitacionService habitacionService;

    /**
     * Constructor para inyección de dependencias.
     * @param dataStore
     * @param habitacionService
     */
    public ReservaService(DataStore dataStore, HabitacionService habitacionService) {
        this.dataStore = dataStore;
        this.habitacionService = habitacionService;
    }

    /**
     * Método para crear una nueva reserva.
     * Realiza validaciones de fechas, disponibilidad de la habitación y capacidad antes de crear la reserva.
     * @param dto
     * @return
     */
    public Reserva crearReserva(ReservaRequestDTO dto) {
        // Validar fechas
        if (!dto.getFechaInicio().isBefore(dto.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        if (dto.getFechaInicio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado.");
        }

        Huesped huesped = buscarHuespedOFallar(dto.getIdHuesped());
        Habitacion habitacion = habitacionService.obtenerOFallar(dto.getNumeroHabitacion());

        if (habitacion.getEstadoHabitacion() == EstadoHabitacion.FUERA_DE_SERVICIO
                || habitacion.getEstadoHabitacion() == EstadoHabitacion.MANTENIMIENTO) {
            throw new IllegalStateException(
                    "Habitación " + dto.getNumeroHabitacion() + " no disponible: "
                            + habitacion.getEstadoHabitacion());
        }

        // Anti-duplicados: verificar solapamiento de fechas
        verificarDisponibilidadEnFechas(
                dto.getNumeroHabitacion(), dto.getFechaInicio(), dto.getFechaFin(), null);

        int totalPersonas = dto.getAdultos() + dto.getNinos();
        if (totalPersonas > habitacion.getCapacidad()) {
            throw new IllegalArgumentException(
                    "Capacidad máxima: " + habitacion.getCapacidad()
                            + ". Personas solicitadas: " + totalPersonas);
        }

        String codigoReserva = generarCodigoReserva();
        Reserva reserva = new Reserva(codigoReserva, dto.getFechaInicio(), dto.getFechaFin(),
                dto.getAdultos(), dto.getNinos(), huesped, habitacion);

        habitacion.cambiarEstado(EstadoHabitacion.RESERVADA);
        dataStore.getReservas().add(reserva);
        return reserva;
    }

    /**
     * Método para confirmar una reserva. Solo se pueden confirmar reservas que estén en estado RESERVADA.
     * Al confirmar, se cambia el estado de la reserva a CONFIRMADA.
     * @param idReserva
     * @return
     */
    public Reserva confirmarReserva(String idReserva) {
        Reserva reserva = obtenerOFallar(idReserva);
        if (reserva.getEstadoReserva() != EstadoReserva.RESERVADA) {
            throw new IllegalStateException(
                    "Solo se confirman reservas en estado RESERVADA. Estado actual: "
                            + reserva.getEstadoReserva());
        }
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
        return reserva;
    }

    /**
     * Método para cancelar una reserva.
     * No se pueden cancelar reservas que estén en estado CHECKED_IN o FINALIZADA.
     * @param idReserva
     * @return
     */
    public Reserva cancelarReserva(String idReserva) {
        Reserva reserva = obtenerOFallar(idReserva);
        if (reserva.getEstadoReserva() == EstadoReserva.CHECKED_IN
                || reserva.getEstadoReserva() == EstadoReserva.FINALIZADA) {
            throw new IllegalStateException(
                    "No se puede cancelar una reserva en estado: " + reserva.getEstadoReserva());
        }
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);
        Habitacion hab = reserva.getHabitacion();
        if (hab.getEstadoHabitacion() == EstadoHabitacion.RESERVADA) {
            hab.cambiarEstado(EstadoHabitacion.DISPONIBLE);
        }
        return reserva;
    }

    /**
     * Método para marcar una reserva como NO_SHOW.
     * Solo se pueden marcar como NO_SHOW reservas que estén en estado RESERVADA o CONFIRMADA.
     * @param idReserva
     * @return
     */
    public Reserva marcarNoShow(String idReserva) {
        Reserva reserva = obtenerOFallar(idReserva);
        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA
                && reserva.getEstadoReserva() != EstadoReserva.RESERVADA) {
            throw new IllegalStateException("Solo se marca NO_SHOW en reservas RESERVADA o CONFIRMADA.");
        }
        reserva.setEstadoReserva(EstadoReserva.NO_SHOW);
        Habitacion hab = reserva.getHabitacion();
        if (hab.getEstadoHabitacion() == EstadoHabitacion.RESERVADA) {
            hab.cambiarEstado(EstadoHabitacion.DISPONIBLE);
        }
        return reserva;
    }

    /**
     * Método para listar todas las reservas registradas en el sistema.
     * @return
     */
    public List<Reserva> listarTodas() {
        return dataStore.getReservas();
    }

    /**
     * Método para listar las reservas asociadas a un huésped específico, filtrando por el ID del huésped.
     * @param idHuesped
     * @return
     */
    public List<Reserva> listarPorHuesped(String idHuesped) {
        return dataStore.getReservas().stream()
                .filter(r -> r.getHuesped().getIdHuesped().equals(idHuesped))
                .collect(Collectors.toList());
    }

    public Optional<Reserva> buscarPorId(String idReserva) {
        return dataStore.getReservas().stream()
                .filter(r -> r.getIdReserva().equals(idReserva))
                .findFirst();
    }

    /**
     * Método para obtener una reserva por su ID.
     * Si no se encuentra la reserva, se lanza una excepción con un mensaje claro.
     * @param idReserva
     * @return
     */
    public Reserva obtenerOFallar(String idReserva) {
        return buscarPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la reserva con ID: " + idReserva));
    }

    /**
     * Método para buscar un huésped por su ID.
     * Si no se encuentra el huésped, se lanza una excepción con un mensaje claro
     * @param idHuesped
     * @return
     */
    private Huesped buscarHuespedOFallar(String idHuesped) {
        return dataStore.getHuespedes().stream()
                .filter(h -> h.getIdHuesped().equals(idHuesped))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el huésped con ID: " + idHuesped));
    }

    /**
     * Método para generar un código de reserva único.
     * El formato del código es "RES-AAAA-NNNNNN", donde "AAAA" es el año actual y "NNNNNN" es un número consecutivo de 6 dígitos.
     * @return
     */
    private String generarCodigoReserva() {
        int anio = LocalDate.now().getYear();
        int consecutivo = dataStore.getReservas().size() + 1;
        String codigo;
        do {
            codigo = String.format("RES-%d-%06d", anio, consecutivo++);
        } while (existeCodigoReserva(codigo));
        return codigo;
    }

    /**
     * Método para verificar si ya existe una reserva con el mismo código o ID.
     * @param codigo
     * @return
     */
    private boolean existeCodigoReserva(String codigo) {
        return dataStore.getReservas().stream()
                .anyMatch(r -> codigo.equals(r.getCodigoReserva()) || codigo.equals(r.getIdReserva()));
    }

    /**
     * Método para verificar si una habitación está disponible en un rango de fechas específico,
     * excluyendo una reserva en particular (útil para modificaciones).
     * @param numeroHabitacion
     * @param inicio
     * @param fin
     * @param excluirIdReserva
     */
    public void verificarDisponibilidadEnFechas(int numeroHabitacion,
                                                LocalDate inicio, LocalDate fin,
                                                String excluirIdReserva) {
        boolean haySolapamiento = dataStore.getReservas().stream()
                .filter(r -> r.getHabitacion().getNumeroHabitacion() == numeroHabitacion)
                .filter(r -> r.getEstadoReserva() != EstadoReserva.CANCELADA
                        && r.getEstadoReserva() != EstadoReserva.NO_SHOW
                        && r.getEstadoReserva() != EstadoReserva.FINALIZADA)
                .filter(r -> excluirIdReserva == null || !r.getIdReserva().equals(excluirIdReserva))
                .anyMatch(r -> inicio.isBefore(r.getFechaFin()) && fin.isAfter(r.getFechaInicio()));

        if (haySolapamiento) {
            throw new IllegalStateException(
                    "Habitación " + numeroHabitacion
                            + " ya tiene reserva activa en las fechas: " + inicio + " → " + fin);
        }
    }
}

