package co.edu.uniquindio.sistemahotelmanager.data;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.sistemahotelmanager.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que actúa como un almacén de datos en memoria para el sistema de gestión hotelera.
 * Contiene listas para almacenar información sobre habitaciones, categorías, huéspedes, recepcionistas,
 * administradores, personal de limpieza, reservas, estadías y pagos.
 */
@Component
public class DataStore {

    /**
     * Listas para almacenar los datos de las diferentes entidades del sistema.
     * Estas listas actúan como un repositorio en memoria para facilitar el acceso y la manipulación de los datos durante la ejecución del programa.
     */
    private final List<Habitacion> habitaciones   = new ArrayList<>();
    private final List<CategoriaHabitacion> categorias  = new ArrayList<>();
    private final List<Huesped>          huespedes      = new ArrayList<>();
    private final List<Recepcionista>    recepcionistas = new ArrayList<>();
    private final List<Administrador>    administradores= new ArrayList<>();
    private final List<PersonalLimpieza> personalLimpieza = new ArrayList<>();
    private final List<Reserva>          reservas       = new ArrayList<>();
    private final List<Estadia>          estadias       = new ArrayList<>();
    private final List<Pago>             pagos          = new ArrayList<>();

    /**
     * Métodos getters para acceder a las listas de datos.
     * Estos métodos permiten obtener las listas completas de cada entidad, facilitando la manipulación y consulta de los datos almacenados en el DataStore.
     * @return
     */
    public List<Habitacion>        getHabitaciones()    { return habitaciones; }
    public List<CategoriaHabitacion> getCategorias()    { return categorias; }
    public List<Huesped>           getHuespedes()       { return huespedes; }
    public List<Recepcionista>     getRecepcionistas()  { return recepcionistas; }
    public List<Administrador>     getAdministradores() { return administradores; }
    public List<PersonalLimpieza>  getPersonalLimpieza(){ return personalLimpieza; }
    public List<Reserva>           getReservas()        { return reservas; }
    public List<Estadia>           getEstadias()        { return estadias; }
    public List<Pago>              getPagos()           { return pagos; }
}

