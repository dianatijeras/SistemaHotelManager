package co.edu.uniquindio.sistemahotelmanager.model;

public class Huesped extends Usuario{

    private String idHuesped;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

    /**
     * Constructor para crear un nuevo huésped
     * @param idHuesped
     * @param nombre
     * @param apellido
     * @param telefono
     * @param correo
     * @param username
     * @param password
     */
    public Huesped(String idHuesped, String nombre, String apellido,
                   String telefono, String correo, String username, String password) {
        super(idHuesped, username, password, Rol.RECEPCIONISTA);
        this.idHuesped = idHuesped;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * Método para actualizar los datos de contacto del huésped
     * @param telefono
     * @param correo
     */
    public void actualizarDatos(String telefono, String correo) {
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * Método para obtener el nombre completo del huésped
     * @return
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido; }

    /**
     * Método para obtener el ID del huésped
     * @return
     */
    public String getIdHuesped() {
        return idHuesped; }

    /**
     * Método para establecer el ID del huésped
     * @param idHuesped
     */
    public void setIdHuesped(String idHuesped) {
        this.idHuesped = idHuesped; }

    /**
     * Método para obtener el nombre del huésped
     * @return
     */
    public String getNombre() {
        return nombre; }

    /**
     * Método para establecer el nombre del huésped
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    /**
     * Método para obtener el apellido del huésped
     * @return
     */
    public String getApellido() {
        return apellido; }

    /**
     * Método para establecer el apellido del huésped
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido; }

    /**
     * Método para obtener el teléfono del huésped
     * @return
     */
    public String getTelefono() {
        return telefono; }

    /**
     * Método para establecer el teléfono del huésped
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono; }

    /**
     * Método para obtener el correo del huésped
     * @return
     */
    public String getCorreo() {
        return correo; }

    /**
     *  Método para establecer el correo del huésped
     * @param correo
     */
    public void setCorreo(String correo) {
        this.correo = correo; }
}
