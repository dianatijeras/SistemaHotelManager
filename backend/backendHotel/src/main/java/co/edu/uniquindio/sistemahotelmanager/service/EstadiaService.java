package co.edu.uniquindio.sistemahotelmanager.service;

import co.edu.uniquindio.sistemahotelmanager.enums.EstadoEstadia;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoHabitacion;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoReserva;
import co.edu.uniquindio.sistemahotelmanager.model.ConsumoEstadia;
import co.edu.uniquindio.sistemahotelmanager.model.Estadia;
import co.edu.uniquindio.sistemahotelmanager.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la lógica de negocio relacionada con las estadías en el sistema de gestión hotelera.
 * Permite realizar check-in, check-out, agregar consumos a una estadía, y listar estadías según diferentes criterios.
 */
@Service
public class EstadiaService {
    private final DataStore dataStore;
    private final ReservaService reservaService;

    /**
     * Constructor para inyectar las dependencias necesarias en el servicio de estadías.
     * @param dataStore
     * @param reservaService
     */
    public EstadiaService(DataStore dataStore, ReservaService reservaService) {
        this.dataStore = dataStore;
        this.reservaService = reservaService;
    }

    /**
     * Método para realizar el check-in de una reserva, creando una nueva estadía asociada a la reserva y actualizando los estados correspondientes de la reserva y la habitación.
     * Se realizan varias validaciones para asegurar que el check-in se pueda realizar correctamente.
     * @param idReserva
     * @return
     */
    public Estadia realizarCheckIn(String idReserva) {
        if (idReserva == null || idReserva.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la reserva para realizar el check-in.");
        }

        String idReservaLimpio = idReserva.trim();
        Reserva reserva = reservaService.obtenerOFallar(idReservaLimpio);

        if (reserva.getEstadoReserva() != EstadoReserva.RESERVADA
                && reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "No se puede hacer check-in. Estado de reserva: " + reserva.getEstadoReserva());
        }

        boolean yaExisteEstadiaParaReserva = dataStore.getEstadias().stream()
                .anyMatch(e -> e.getReserva().getIdReserva().equals(idReservaLimpio)
                        && e.getEstadoEstadia() == EstadoEstadia.EN_CURSO);
        if (yaExisteEstadiaParaReserva) {
            throw new IllegalStateException("Ya existe una estadía activa para la reserva: " + idReservaLimpio);
        }

        boolean habitacionOcupadaPorOtraEstadia = dataStore.getEstadias().stream()
                .anyMatch(e -> e.getEstadoEstadia() == EstadoEstadia.EN_CURSO
                        && e.getReserva().getHabitacion().getNumeroHabitacion() == reserva.getHabitacion().getNumeroHabitacion()
                        && !e.getReserva().getIdReserva().equals(idReservaLimpio));
        if (habitacionOcupadaPorOtraEstadia) {
            throw new IllegalStateException(
                    "La habitación " + reserva.getHabitacion().getNumeroHabitacion()
                            + " ya tiene una estadía activa.");
        }

        if (reserva.getHabitacion().getEstadoHabitacion() == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException(
                    "No se puede hacer check-in porque la habitación ya está OCUPADA.");
        }

        if (reserva.getHabitacion().getEstadoHabitacion() == EstadoHabitacion.MANTENIMIENTO
                || reserva.getHabitacion().getEstadoHabitacion() == EstadoHabitacion.FUERA_DE_SERVICIO) {
            throw new IllegalStateException(
                    "No se puede hacer check-in porque la habitación está en estado: "
                            + reserva.getHabitacion().getEstadoHabitacion());
        }

        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(reserva.getFechaFin())) {
            throw new IllegalStateException(
                    "No se puede hacer check-in. La fecha de salida ya pasó: " + reserva.getFechaFin());
        }

        String idEstadia = "EST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Estadia estadia = new Estadia(idEstadia, reserva);
        estadia.iniciar();

        reserva.setEstadoReserva(EstadoReserva.CHECKED_IN);
        reserva.setFechaEstadia(LocalDate.now());
        reserva.getHabitacion().cambiarEstado(EstadoHabitacion.OCUPADA);

        dataStore.getEstadias().add(estadia);
        return estadia;
    }

    /**
     * Método para realizar el check-out de una estadía, finalizando la estadía y actualizando los estados correspondientes de la reserva y la habitación.
     * @param idEstadia
     * @return
     */
    public Estadia realizarCheckOut(String idEstadia) {
        Estadia estadia = obtenerOFallar(idEstadia);

        if (estadia.getEstadoEstadia() != EstadoEstadia.EN_CURSO) {
            throw new IllegalStateException(
                    "No se puede hacer check-out. Estado estadía: " + estadia.getEstadoEstadia());
        }

        estadia.finalizar();

        Reserva reserva = estadia.getReserva();
        reserva.setEstadoReserva(EstadoReserva.FINALIZADA);
        reserva.getHabitacion().cambiarEstado(EstadoHabitacion.EN_LIMPIEZA);

        return estadia;
    }

    /**
     * Método para agregar un consumo a una estadía, validando los datos del consumo y asegurando que la estadía esté en curso antes de agregar el consumo.
     * @param dto
     * @return
     */
    public Estadia agregarConsumo(ConsumoRequestDTO dto) {
        validarConsumo(dto);
        Estadia estadia = obtenerOFallar(dto.getIdEstadia());

        if (estadia.getEstadoEstadia() != EstadoEstadia.EN_CURSO) {
            throw new IllegalStateException(
                    "Solo se agregan consumos a estadías EN_CURSO. Estado: " + estadia.getEstadoEstadia());
        }

        String idConsumo = "CON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ConsumoEstadia consumo = new ConsumoEstadia(
                idConsumo, dto.getDescripcion().trim(), LocalDate.now(),
                dto.getCantidad(), dto.getPrecioUnitario());

        estadia.agregarConsumo(consumo);
        return estadia;
    }

    /**
     * Método privado para validar los datos de un consumo antes de agregarlo a una estadía.
     * Se verifican que los datos no sean nulos o vacíos, y que la cantidad y el precio unitario sean mayores a 0.
     * @param dto
     */
    private void validarConsumo(ConsumoRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del consumo son obligatorios.");
        }
        if (dto.getIdEstadia() == null || dto.getIdEstadia().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la estadía para registrar el consumo.");
        }
        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del consumo es obligatoria.");
        }
        if (dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad del consumo debe ser mayor a 0.");
        }
        if (dto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario del consumo debe ser mayor a 0.");
        }
    }

    /**
     * Método para listar todas las estadías registradas en el sistema, sin aplicar ningún filtro.
     * @return
     */
    public List<Estadia> listarTodas() {
        return dataStore.getEstadias();
    }

    /**
     * Método para listar las estadías que están en curso o finalizadas, lo que indica que están listas para ser pagadas.
     * @return
     */
    public List<Estadia> listarParaPagos() {
        return dataStore.getEstadias().stream()
                .filter(e -> e.getEstadoEstadia() == EstadoEstadia.EN_CURSO
                        || e.getEstadoEstadia() == EstadoEstadia.FINALIZADA)
                .collect(Collectors.toList());
    }

    /**
     * Método para listar las estadías que están actualmente en curso, lo que indica que los huéspedes ya hicieron check-in pero aún no han hecho check-out.
     * @return
     */
    public List<Estadia> listarEnCurso() {
        return dataStore.getEstadias().stream()
                .filter(e -> e.getEstadoEstadia() == EstadoEstadia.EN_CURSO)
                .collect(Collectors.toList());
    }

    /**
     * Método para buscar una estadía por su ID, devolviendo un Optional que puede contener la estadía si se encuentra o estar vacío si no se encuentra.
     * @param idEstadia
     * @return
     */
    public Optional<Estadia> buscarPorId(String idEstadia) {
        return dataStore.getEstadias().stream()
                .filter(e -> e.getIdEstadia().equals(idEstadia))
                .findFirst();
    }

    /**
     * Método para obtener una estadía por su ID, lanzando una excepción si no se encuentra la estadía con el ID proporcionado.
     * Este método es útil para asegurar que se obtiene una estadía válida antes de realizar operaciones que requieren una estadía existente.
     * @param idEstadia
     * @return
     */
    public Estadia obtenerOFallar(String idEstadia) {
        return buscarPorId(idEstadia)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la estadía con ID: " + idEstadia));
    }
}
