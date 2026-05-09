package co.edu.uniquindio.sistemahotelmanager.service;

import co.edu.uniquindio.sistemahotelmanager.data.DataStore;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.enums.TipoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.model.Habitacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las operaciones relacionadas con las habitaciones del hotel.
 */
@Service
public class HabitacionService {

    private final DataStore dataStore;

    /**
     * Constructor para HabitacionService
     * @param dataStore
     */
    public HabitacionService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Método para listar todas las habitaciones del hotel
     * @return
     */
    public List<Habitacion> listarTodas() {
        return dataStore.getHabitaciones();
    }

    /**
     * Método para buscar una habitación por su número
     * @param numero
     * @return
     */
    public Optional<Habitacion> buscarPorNumero(int numero) {
        return dataStore.getHabitaciones().stream()
                .filter(h -> h.getNumeroHabitacion() == numero)
                .findFirst();
    }

    /**
     * Método para listar solo las habitaciones que están disponibles para reserva
     * @return
     */
    public List<Habitacion> listarDisponibles() {
        return dataStore.getHabitaciones().stream()
                .filter(Habitacion::estaDisponible)
                .collect(Collectors.toList());
    }

    /**
     * Método para listar habitaciones por tipo (SENCILLA, DOBLE, SUITE)
     * @param tipo
     * @return
     */
    public List<Habitacion> listarPorTipo(TipoHabitacion tipo) {
        return dataStore.getHabitaciones().stream()
                .filter(h -> h.getTipoHabitacion() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Método para listar habitaciones que están pendientes de limpieza (estado EN_LIMPIEZA)
     * @return
     */
    public List<Habitacion> listarPendientesLimpieza() {
        return dataStore.getHabitaciones().stream()
                .filter(h -> h.getEstadoHabitacion() == EstadoHabitacion.EN_LIMPIEZA)
                .collect(Collectors.toList());
    }

    /**
     * Método para cambiar el estado de una habitación, validando que el nuevo estado sea válido y que la habitación exista
     * @param numeroHabitacion
     * @param nuevoEstado
     * @return
     */
    public Habitacion cambiarEstado(int numeroHabitacion, EstadoHabitacion nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la habitación es obligatorio.");
        }
        Habitacion hab = obtenerOFallar(numeroHabitacion);
        hab.cambiarEstado(nuevoEstado);
        return hab;
    }

    /**
     * Método para marcar una habitación como en limpieza, validando que solo se pueda marcar desde estados DISPONIBLE u OCUPADA
     * @param numeroHabitacion
     * @return
     */
    public Habitacion marcarEnLimpieza(int numeroHabitacion) {
        Habitacion hab = obtenerOFallar(numeroHabitacion);
        if (hab.getEstadoHabitacion() != EstadoHabitacion.DISPONIBLE
                && hab.getEstadoHabitacion() != EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException(
                    "La habitación " + numeroHabitacion + " no puede pasar a limpieza desde: "
                            + hab.getEstadoHabitacion());
        }
        hab.cambiarEstado(EstadoHabitacion.EN_LIMPIEZA);
        return hab;
    }

    /**
     * Método para marcar una habitación como disponible, validando que solo se pueda marcar desde estado EN_LIMPIEZA
     * @param numeroHabitacion
     * @return
     */
    public Habitacion marcarDisponible(int numeroHabitacion) {
        Habitacion hab = obtenerOFallar(numeroHabitacion);
        if (hab.getEstadoHabitacion() != EstadoHabitacion.EN_LIMPIEZA) {
            throw new IllegalStateException(
                    "La habitación " + numeroHabitacion + " no está en limpieza. Estado: "
                            + hab.getEstadoHabitacion());
        }
        hab.cambiarEstado(EstadoHabitacion.DISPONIBLE);
        return hab;
    }

    /**
     * Método para marcar una habitación como en mantenimiento, validando que solo se pueda marcar desde estados DISPONIBLE o EN_LIMPIEZA
     * @param numeroHabitacion
     * @return
     */
    public Habitacion marcarMantenimiento(int numeroHabitacion) {
        Habitacion hab = obtenerOFallar(numeroHabitacion);
        hab.cambiarEstado(EstadoHabitacion.MANTENIMIENTO);
        return hab;
    }

    /**
     * Método para marcar una habitación como fuera de servicio, validando que solo se pueda marcar desde estados DISPONIBLE o EN_LIMPIEZA
     * @param numeroHabitacion
     * @return
     */
    public Habitacion marcarFueraDeServicio(int numeroHabitacion) {
        Habitacion hab = obtenerOFallar(numeroHabitacion);
        hab.cambiarEstado(EstadoHabitacion.FUERA_DE_SERVICIO);
        return hab;
    }

    /**
     * Método para obtener una habitación por su número o lanzar una excepción si no existe
     * @param numero
     * @return
     */
    public Habitacion obtenerOFallar(int numero) {
        return buscarPorNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la habitación número: " + numero));
    }
}

