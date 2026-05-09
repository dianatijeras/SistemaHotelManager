package co.edu.uniquindio.sistemahotelmanager.model;

import co.edu.uniquindio.sistemahotelmanager.enums.Rol;

/**
 * Clase que representa a un administrador del sistema, hereda de Usuario.
 */
public class Administrador extends Usuario{

    private String idAdministrador;
    private String nombre;
    private String apellido;

    /**
     * Constructor para crear un nuevo administrador
     * @param idAdministrador
     * @param nombre
     * @param apellido
     * @param username
     * @param password
     */
    public Administrador(String idAdministrador, String nombre, String apellido,
                         String username, String password) {
        super(idAdministrador, username, password, Rol.ADMIN);
        this.idAdministrador = idAdministrador;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * Método para obtener el nombre completo del administrador
     * @return
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Método para obtener el ID del administrador
     * @return
     */
    public String getIdAdministrador() {
        return idAdministrador;
    }

    /**
     * Método para establecer el ID del administrador
     * @param id
     */
    public void setIdAdministrador(String id) {
        this.idAdministrador = id;
    }

    /**
     * Método para obtener el nombre del administrador
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del administrador
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener el apellido del administrador
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Método para establecer el apellido del administrador
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
