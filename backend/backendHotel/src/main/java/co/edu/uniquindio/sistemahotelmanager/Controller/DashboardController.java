package co.edu.uniquindio.sistemahotelmanager.Controller;

import co.edu.uniquindio.sistemahotelmanager.data.DataStore;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoEstadia;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoPago;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoReserva;
import co.edu.uniquindio.sistemahotelmanager.model.Pago;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DataStore dataStore;

    /**
     * Constructor para inyectar el DataStore.
     * @param dataStore
     */
    public DashboardController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Endpoint para obtener estadísticas clave del hotel, como número de habitaciones disponibles, ocupadas, reservas activas, pagos pendientes, entre otros.
     * @return
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long habitacionesDisponibles = dataStore.getHabitaciones().stream()
                .filter(h -> h.getEstadoHabitacion() == EstadoHabitacion.DISPONIBLE)
                .count();
        long habitacionesOcupadas = dataStore.getHabitaciones().stream()
                .filter(h -> h.getEstadoHabitacion() == EstadoHabitacion.OCUPADA)
                .count();
        long habitacionesEnLimpieza = dataStore.getHabitaciones().stream()
                .filter(h -> h.getEstadoHabitacion() == EstadoHabitacion.EN_LIMPIEZA)
                .count();
        long reservasActivas = dataStore.getReservas().stream()
                .filter(r -> r.getEstadoReserva() == EstadoReserva.RESERVADA
                        || r.getEstadoReserva() == EstadoReserva.CONFIRMADA
                        || r.getEstadoReserva() == EstadoReserva.CHECKED_IN)
                .count();
        long reservasEnProceso = dataStore.getReservas().stream()
                .filter(r -> r.getEstadoReserva() == EstadoReserva.RESERVADA
                        || r.getEstadoReserva() == EstadoReserva.CONFIRMADA)
                .count();
        long estadiasEnCurso = dataStore.getEstadias().stream()
                .filter(e -> e.getEstadoEstadia() == EstadoEstadia.EN_CURSO)
                .count();
        long pagosPendientes = dataStore.getPagos().stream()
                .filter(p -> p.getEstadoPago() == EstadoPago.PENDIENTE)
                .count();
        double totalPagado = dataStore.getPagos().stream()
                .filter(p -> p.getEstadoPago() == EstadoPago.PAGADO)
                .mapToDouble(Pago::getMonto)
                .sum();
        long checkoutsHoy = dataStore.getEstadias().stream()
                .filter(e -> e.getEstadoEstadia() == EstadoEstadia.EN_CURSO)
                .filter(e -> e.getReserva().getFechaFin().isEqual(LocalDate.now()))
                .count();

        stats.put("totalHabitaciones", dataStore.getHabitaciones().size());
        stats.put("habitacionesDisponibles", habitacionesDisponibles);
        stats.put("habitacionesOcupadas", habitacionesOcupadas);
        stats.put("habitacionesEnLimpieza", habitacionesEnLimpieza);
        stats.put("totalHuespedes", dataStore.getHuespedes().size());
        stats.put("totalReservas", dataStore.getReservas().size());
        stats.put("reservasActivas", reservasActivas);
        stats.put("reservasEnProceso", reservasEnProceso);
        stats.put("estadiasEnCurso", estadiasEnCurso);
        stats.put("pagosPendientes", pagosPendientes);
        stats.put("totalPagado", totalPagado);
        stats.put("checkoutsHoy", checkoutsHoy);
        stats.put("totalUsuarios", dataStore.getAdministradores().size()
                + dataStore.getRecepcionistas().size()
                + dataStore.getPersonalLimpieza().size());
        stats.put("totalRecepcionistas", dataStore.getRecepcionistas().size());
        stats.put("totalPersonalLimpieza", dataStore.getPersonalLimpieza().size());

        return ResponseEntity.ok(stats);
    }
}
