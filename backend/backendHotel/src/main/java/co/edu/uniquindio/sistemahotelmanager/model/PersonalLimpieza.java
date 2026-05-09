package co.edu.uniquindio.sistemahotelmanager.model;

import co.edu.uniquindio.sistemahotelmanager.enums.Rol;

/**
 * Clase que representa al personal de limpieza del hotel, hereda de Usuario.
 */
public class PersonalLimpieza extends Usuario{

    private String idPersonalLimpieza;
    private String nombre;
    private String apellido;

    /**
     * Constructor para PersonalLimpieza
     * @param idPersonalLimpieza
     * @param nombre
     * @param apellido
     * @param username
     * @param password
     */
    public PersonalLimpieza(String idPersonalLimpieza, String nombre, String apellido,
                            String username, String password) {
        super(idPersonalLimpieza, username, password, Rol.PERSONAL_LIMPIEZA);
        this.idPersonalLimpieza = idPersonalLimpieza;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * Método para obtener el nombre completo del personal de limpieza
     * @return
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Método para obtener el ID del personal de limpieza
     * @return
     */
    public String getIdPersonalLimpieza() {
        return idPersonalLimpieza;
    }

    /**
     * Método para establecer el ID del personal de limpieza
     * @param id
     */
    public void setIdPersonalLimpieza(String id) {
        this.idPersonalLimpieza = id;
    }

    /**
     * Método para obtener el nombre del personal de limpieza
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del personal de limpieza
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener el apellido del personal de limpieza
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Método para establecer el apellido del personal de limpieza
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
