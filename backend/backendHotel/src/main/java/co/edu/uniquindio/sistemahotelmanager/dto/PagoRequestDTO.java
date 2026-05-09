package co.edu.uniquindio.sistemahotelmanager.dto;

import co.edu.uniquindio.sistemahotelmanager.enums.MetodoPago;

/**
 * DTO para representar la información de una solicitud de pago en el sistema de gestión hotelera.
 */
public class PagoRequestDTO {

    private String idEstadia;
    private double monto;
    private MetodoPago metodoPago;
    private String referenciaPasarela;

    /**
     * Metodo para obtener el ID de la estadía asociada al pago.
     * @return
     */
    public String getIdEstadia() {
        return idEstadia;
    }

    /**
     * Metodo para establecer el ID de la estadía asociada al pago.
     * @param id
     */
    public void setIdEstadia(String id) {
        this.idEstadia = id;
    }

    /**
     * Metodo para obtener el monto del pago.
     * @return
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Metodo para establecer el monto del pago.
     * @param m
     */
    public void setMonto(double m) {
        this.monto = m;
    }

    /**
     * Metodo para obtener el método de pago utilizado en la solicitud de pago.
     * @return
     */
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    /**
     * Metodo para establecer el método de pago utilizado en la solicitud de pago.
     * @param m
     */
    public void setMetodoPago(MetodoPago m) {
        this.metodoPago = m;
    }

    /**
     * Metodo para obtener la referencia de la pasarela de pago, si el método de pago es una pasarela.
     * @return
     */
    public String getReferenciaPasarela() {
        return referenciaPasarela;
    }

    /**
     * Metodo para establecer la referencia de la pasarela de pago, si el método de pago es una pasarela.
     * @param r
     */
    public void setReferenciaPasarela(String r) {
        this.referenciaPasarela = r;
    }
}
