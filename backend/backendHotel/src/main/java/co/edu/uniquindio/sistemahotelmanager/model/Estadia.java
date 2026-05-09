package co.edu.uniquindio.sistemahotelmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Estadia {

    private String idEstadia;
    private LocalDateTime fechaCheckIn;
    private LocalDateTime fechaCheckOut;
    private double totalHabitacion;
    private double totalConsumos;
    private double totalFinal;
    private EstadoEstadia estadoEstadia;
    private Reserva reserva;
    private List<ConsumoEstadia> consumos;

    /**
     * Constructor para crear una nueva estadía a partir de una reserva. El estado inicial será PROGRAMADA, se calculará el total de la habitación y se inicializarán los consumos.
     * @param idEstadia
     * @param reserva
     */
    public Estadia(String idEstadia, Reserva reserva) {
        this.idEstadia = idEstadia;
        this.reserva = reserva;
        this.consumos = new ArrayList<>();
        this.estadoEstadia = EstadoEstadia.PROGRAMADA;
        this.totalHabitacion = reserva.calcularTotalHabitacion();
        this.totalConsumos = 0.0;
        this.totalFinal = this.totalHabitacion;
    }

    /**
     * Método para iniciar la estadía, registrando la fecha de check-in y cambiando el estado a EN_CURSO.
     */
    public void iniciar() {
        this.fechaCheckIn = LocalDateTime.now();
        this.estadoEstadia = EstadoEstadia.EN_CURSO;
    }

    /**
     * Método para finalizar la estadía, registrando la fecha de check-out, cambiando el estado a FINALIZADA y recalculando los totales para reflejar cualquier consumo adicional registrado durante la estadía.
     */
    public void finalizar() {
        this.fechaCheckOut = LocalDateTime.now();
        this.estadoEstadia = EstadoEstadia.FINALIZADA;
        recalcularTotales();
    }

    /**
     * Método para agregar un consumo a la estadía, añadiendo el consumo a la lista de consumos y recalculando los totales para reflejar el nuevo consumo.
     * @param consumo
     */
    public void agregarConsumo(ConsumoEstadia consumo) {
        this.consumos.add(consumo);
        recalcularTotales();
    }

    /**
     * Método para recalcular los totales de la estadía, sumando el total de consumos al total de la habitación para obtener el total final. Este método se llama cada vez que se agrega un nuevo consumo o cuando se finaliza la estadía para asegurar que los totales estén siempre actualizados.
     */
    public void recalcularTotales() {
        this.totalConsumos = consumos.stream()
                .mapToDouble(ConsumoEstadia::calcularSubtotal)
                .sum();
        this.totalFinal = this.totalHabitacion + this.totalConsumos;
    }

    /**
     * Método para obtener el ID de la estadía
     * @return
     */
    public String getIdEstadia() {
        return idEstadia; }

    /**
     * Método para establecer el ID de la estadía
     * @param id
     */
    public void setIdEstadia(String id) {
        this.idEstadia = id; }

    /**
     * Método para obtener la fecha de check-in de la estadía
     * @return
     */
    public LocalDateTime getFechaCheckIn() {
        return fechaCheckIn; }

    /**
     * Método para establecer la fecha de check-in de la estadía
     * @param f
     */
    public void setFechaCheckIn(LocalDateTime f) {
        this.fechaCheckIn = f; }

    /**
     * Método para obtener la fecha de check-out de la estadía
     * @return
     */
    public LocalDateTime getFechaCheckOut() {
        return fechaCheckOut; }

    /**
     * Método para establecer la fecha de check-out de la estadía
     * @param f
     */
    public void setFechaCheckOut(LocalDateTime f) {
        this.fechaCheckOut = f; }

    /**
     * Método para obtener el total de la habitación de la estadía
     * @return
     */
    public double getTotalHabitacion() {
        return totalHabitacion; }

    /**
     * Método para establecer el total de la habitación de la estadía
     * @param t
     */
    public void setTotalHabitacion(double t) {
        this.totalHabitacion = t; }

    /**
     * Método para obtener el total de consumos de la estadía
     * @return
     */
    public double getTotalConsumos() {
        return totalConsumos; }

    /**
     * Método para establecer el total de consumos de la estadía
     * @param t
     */
    public void setTotalConsumos(double t) {
        this.totalConsumos = t; }

    /**
     * Método para obtener el total final de la estadía (total habitación + total consumos)
     * @return
     */
    public double getTotalFinal() {
        return totalFinal; }

    /**
     * Método para establecer el total final de la estadía (total habitación + total consumos)
     * @param t
     */
    public void setTotalFinal(double t) {
        this.totalFinal = t; }

    /**
     * Método para obtener el estado de la estadía
     * @return
     */
    public EstadoEstadia getEstadoEstadia() {
        return estadoEstadia; }

    /**
     * Método para establecer el estado de la estadía
     * @param e
     */
    public void setEstadoEstadia(EstadoEstadia e) {
        this.estadoEstadia = e; }

    /**
     * Método para obtener la reserva asociada a la estadía
     * @return
     */
    public Reserva getReserva() {
        return reserva; }

    /**
     * Método para establecer la reserva asociada a la estadía
     * @param r
     */
    public void setReserva(Reserva r) {
        this.reserva = r; }

    /**
     * Método para obtener la lista de consumos asociados a la estadía
     * @return
     */
    public List<ConsumoEstadia> getConsumos() {
        return consumos; }

    /**
     * Método para establecer la lista de consumos asociados a la estadía
     * @param c
     */
    public void setConsumos(List<ConsumoEstadia> c) {
        this.consumos = c; }
}
