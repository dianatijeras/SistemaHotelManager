package co.edu.uniquindio.sistemahotelmanager.Controller;

import co.edu.uniquindio.sistemahotelmanager.dto.EstadoHabitacionRequestDTO;
import co.edu.uniquindio.sistemahotelmanager.enums.TipoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.model.Habitacion;
import co.edu.uniquindio.sistemahotelmanager.service.HabitacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
public class HabitacionController {

    private final HabitacionService habitacionService;

    /**
     * Constructor para inyectar el HabitacionService.
     * @param habitacionService
     */
    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    /**
     * Endpoint para listar todas las habitaciones.
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Habitacion>> listarTodas() {
        return ResponseEntity.ok(habitacionService.listarTodas());
    }

    /**
     * Endpoint para listar solo las habitaciones que están disponibles para reserva o check-in.
     * @return
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Habitacion>> listarDisponibles() {
        return ResponseEntity.ok(habitacionService.listarDisponibles());
    }

    /**
     * Endpoint para listar habitaciones que están pendientes de limpieza (es decir, aquellas que han sido desocupadas pero aún no han sido marcadas como limpias).
     * @return
     */
    @GetMapping("/limpieza")
    public ResponseEntity<List<Habitacion>> listarPendientesLimpieza() {
        return ResponseEntity.ok(habitacionService.listarPendientesLimpieza());
    }

    /**
     * Endpoint para listar habitaciones filtradas por su tipo (SENCILLA, DOBLE, SUITE, etc.).
     * @param tipo
     * @return
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Habitacion>> listarPorTipo(@PathVariable TipoHabitacion tipo) {
        return ResponseEntity.ok(habitacionService.listarPorTipo(tipo));
    }

    /**
     * Endpoint para buscar una habitación por su número único. Devuelve los detalles de la habitación, incluyendo su estado actual y tipo.
     * @param numero
     * @return
     */
    @GetMapping("/{numero}")
    public ResponseEntity<Habitacion> buscarPorNumero(@PathVariable int numero) {
        return habitacionService.buscarPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para cambiar el estado de una habitación (DISPONIBLE, OCUPADA, EN_LIMPIEZA, MANTENIMIENTO, FUERA_DE_SERVICIO). Esto es útil para actualizar el estado de la habitación después de un check-out, durante la limpieza o cuando se reporta un problema.
     * @param numero
     * @param dto
     * @return
     */
    @PutMapping("/{numero}/estado")
    public ResponseEntity<Habitacion> cambiarEstado(@PathVariable int numero,
                                                    @RequestBody EstadoHabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.cambiarEstado(numero, dto.getEstadoHabitacion()));
    }

    /**
     * Endpoint para marcar una habitación como "En Limpieza" después de que un huésped haya hecho check-out, indicando que el personal de limpieza debe preparar la habitación antes de que pueda ser reservada nuevamente.
     * @param numero
     * @return
     */
    @PutMapping("/{numero}/limpieza")
    public ResponseEntity<Habitacion> marcarEnLimpieza(@PathVariable int numero) {
        return ResponseEntity.ok(habitacionService.marcarEnLimpieza(numero));
    }

    /**
     * Endpoint para marcar una habitación como "Disponible" después de que haya sido limpiada y esté lista para ser reservada o ocupada nuevamente.
     * @param numero
     * @return
     */
    @PutMapping("/{numero}/disponible")
    public ResponseEntity<Habitacion> marcarDisponible(@PathVariable int numero) {
        return ResponseEntity.ok(habitacionService.marcarDisponible(numero));
    }

    /**
     * Endpoint para marcar una habitación como "En Mantenimiento" cuando se reporta un problema que requiere reparación, lo que indica que la habitación no está disponible para reservas o check-in hasta que se resuelva el problema.
     * @param numero
     * @return
     */
    @PutMapping("/{numero}/mantenimiento")
    public ResponseEntity<Habitacion> marcarMantenimiento(@PathVariable int numero) {
        return ResponseEntity.ok(habitacionService.marcarMantenimiento(numero));
    }

    /**
     * Endpoint para marcar una habitación como "Fuera de Servicio" cuando se necesita retirar temporalmente del inventario de habitaciones disponibles, ya sea por razones de seguridad, reparaciones mayores o cualquier otra circunstancia que impida su uso.
     * @param numero
     * @return
     */
    @PutMapping("/{numero}/fuera-servicio")
    public ResponseEntity<Habitacion> marcarFueraDeServicio(@PathVariable int numero) {
        return ResponseEntity.ok(habitacionService.marcarFueraDeServicio(numero));
    }
}
