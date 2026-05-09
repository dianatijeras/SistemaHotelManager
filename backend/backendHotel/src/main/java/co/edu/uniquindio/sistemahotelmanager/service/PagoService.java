package co.edu.uniquindio.sistemahotelmanager.service;

import co.edu.uniquindio.sistemahotelmanager.enums.EstadoEstadia;
import co.edu.uniquindio.sistemahotelmanager.enums.EstadoPago;
import co.edu.uniquindio.sistemahotelmanager.model.Estadia;
import co.edu.uniquindio.sistemahotelmanager.model.Pago;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la lógica de negocio relacionada con los pagos en el sistema de gestión hotelera.
 */
@Service
public class PagoService {

    private final DataStore dataStore;
    private final EstadiaService estadiaService;

    /**
     * Constructor del servicio de pagos.
     * @param dataStore
     * @param estadiaService
     */
    public PagoService(DataStore dataStore, EstadiaService estadiaService) {
        this.dataStore = dataStore;
        this.estadiaService = estadiaService;
    }

    /**
     * Registra un nuevo pago para una estadía específica. Verifica que la estadía esté en un estado válido
     * @param dto
     * @return
     */
    public Pago registrarPago(PagoRequestDTO dto) {
        Estadia estadia = estadiaService.obtenerOFallar(dto.getIdEstadia());

        if (estadia.getEstadoEstadia() != EstadoEstadia.EN_CURSO
                && estadia.getEstadoEstadia() != EstadoEstadia.FINALIZADA) {
            throw new IllegalStateException(
                    "Solo se registran pagos para estadías EN_CURSO o FINALIZADAS.");
        }

        boolean yaFuePagado = dataStore.getPagos().stream()
                .anyMatch(p -> p.getEstadia().getIdEstadia().equals(dto.getIdEstadia())
                        && p.getEstadoPago() == EstadoPago.PAGADO);
        if (yaFuePagado) {
            throw new IllegalStateException(
                    "La estadía " + dto.getIdEstadia() + " ya tiene un pago PAGADO registrado.");
        }

        estadia.recalcularTotales();
        double montoFinal = estadia.getTotalFinal();
        if (montoFinal <= 0) {
            throw new IllegalStateException("El total final de la estadía debe ser mayor a 0.");
        }

        String idPago = "PAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Pago pago = new Pago(idPago, montoFinal, dto.getMetodoPago(), estadia);

        if (dto.getReferenciaPasarela() != null && !dto.getReferenciaPasarela().isBlank()) {
            pago.aprobar(dto.getReferenciaPasarela());
        }

        dataStore.getPagos().add(pago);
        return pago;
    }

    /**
     * Aprueba un pago pendiente, cambiando su estado a PAGADO y registrando la referencia de la pasarela de pago.
     * Solo se pueden aprobar pagos que estén en estado PENDIENTE.
     * @param idPago
     * @param referencia
     * @return
     */
    public Pago aprobarPago(String idPago, String referencia) {
        Pago pago = obtenerOFallar(idPago);
        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se aprueban pagos PENDIENTES. Estado: " + pago.getEstadoPago());
        }
        pago.aprobar(referencia);
        return pago;
    }

    /**
     * Rechaza un pago pendiente, cambiando su estado a RECHAZADO y registrando las observaciones correspondientes.
     * @param idPago
     * @param observaciones
     * @return
     */
    public Pago rechazarPago(String idPago, String observaciones) {
        Pago pago = obtenerOFallar(idPago);
        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new IllegalStateException("Solo se rechazan pagos PENDIENTES.");
        }
        pago.rechazar(observaciones);
        return pago;
    }

    /**
     * Reembolsa un pago aprobado, cambiando su estado a REEMBOLSADO.
     * Solo se pueden reembolsar pagos que estén en estado PAGADO.
     * @param idPago
     * @return
     */
    public Pago reembolsarPago(String idPago) {
        Pago pago = obtenerOFallar(idPago);
        pago.reembolsar();
        return pago;
    }

    /**
     * Lista todos los pagos registrados en el sistema.
     * @return
     */
    public List<Pago> listarTodos() {
        return dataStore.getPagos();
    }

    /**
     * Lista los pagos asociados a una estadía específica, filtrando por el ID de la estadía.
     * @param idEstadia
     * @return
     */
    public List<Pago> listarPorEstadia(String idEstadia) {
        return dataStore.getPagos().stream()
                .filter(p -> p.getEstadia().getIdEstadia().equals(idEstadia))
                .collect(Collectors.toList());
    }

    /**
     * Busca un pago por su ID, devolviendo un Optional que puede contener el pago encontrado o estar vacío si no se encuentra ningún pago con ese ID.
     * @param idPago
     * @return
     */
    public Optional<Pago> buscarPorId(String idPago) {
        return dataStore.getPagos().stream()
                .filter(p -> p.getIdPago().equals(idPago))
                .findFirst();
    }

    /**
     * Método privado que busca un pago por su ID y devuelve el pago encontrado.
     * Si no se encuentra ningún pago con ese ID, lanza una excepción IllegalArgumentException con un mensaje indicando que no existe el pago con el ID proporcionado.
     * @param idPago
     * @return
     */
    private Pago obtenerOFallar(String idPago) {
        return buscarPorId(idPago)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el pago con ID: " + idPago));
    }
}

