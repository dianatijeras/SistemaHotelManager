package co.edu.uniquindio.sistemahotelmanager.dto;

import co.edu.uniquindio.sistemahotelmanager.enums.EstadoHabitacion;

/**
 * DTO para representar la solicitud de cambio de estado de una habitación en el sistema de gestión hotelera.
 */
public class EstadoHabitacionRequestDTO {

    private EstadoHabitacion estadoHabitacion;

    /**
     * Obtiene el estado de la habitación.
     * @return
     */
    public EstadoHabitacion getEstadoHabitacion() {
        return estadoHabitacion;
    }

    /**
     * Establece el estado de la habitación.
     * @param estadoHabitacion
     */
    public void setEstadoHabitacion(EstadoHabitacion estadoHabitacion) {
        this.estadoHabitacion = estadoHabitacion;
    }
}
