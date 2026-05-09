package co.edu.uniquindio.sistemahotelmanager.model;

import co.edu.uniquindio.sistemahotelmanager.enums.Rol;

/**
 * Clase abstracta que representa a un usuario del sistema.
 */
public abstract class Usuario {

    private String idUsuario;
    private String username;
    private String password;
    private Rol rol;
    private boolean activo;

    /**
     * Constructor protegido para inicializar un usuario.
     * @param idUsuario
     * @param username
     * @param password
     * @param rol
     */
    protected Usuario(String idUsuario, String username, String password, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = true;
    }

    /**
     * Método para autenticar al usuario comparando el username y password ingresados con los almacenados.
     * @param username
     * @param password
     * @return
     */
    public boolean autenticar(String username, String password){
        return this.username.equals(username) && this.password.equals(password) && this.activo;
    }

    /**
     * Metodo para obtener el ID del usuario.
     * @return
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Metodo para establecer el ID del usuario.
     * @param idUsuario
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Metodo para obtener el nombre de usuario.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo para establecer el nombre de usuario.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Metodo para obtener la contraseña del usuario.
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Metodo para establecer la contraseña del usuario.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metodo para obtener el rol del usuario.
     * @return
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Metodo para establecer el rol del usuario.
     * @param rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Metodo para verificar si el usuario está activo.
     * @return
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Metodo para establecer el estado activo del usuario.
     * @param activo
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

