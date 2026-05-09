package co.edu.uniquindio.sistemahotelmanager.model;

public class Recepcionista  extends Usuario{

    private String idRecepcionista;
    private String nombre;
    private String apellido;
    private TurnoRecepcionista turno;

    /**
     * Constructor para crear un nuevo recepcionista
     * @param idRecepcionista
     * @param nombre
     * @param apellido
     * @param turno
     * @param username
     * @param password
     */
    public Recepcionista(String idRecepcionista, String nombre, String apellido,
                         TurnoRecepcionista turno, String username, String password) {
        super(idRecepcionista, username, password, Rol.RECEPCIONISTA);
        this.idRecepcionista = idRecepcionista;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
    }

    /**
     * Método para obtener el nombre completo del recepcionista
     * @return
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido; }

    /**
     * Método para obtener el ID del recepcionista
     * @return
     */
    public String getIdRecepcionista() {
        return idRecepcionista; }

    /**
     * Método para establecer el ID del recepcionista
     * @param id
     */
    public void setIdRecepcionista(String id) {
        this.idRecepcionista = id; }

    /**
     * Método para obtener el nombre del recepcionista
     * @return
     */
    public String getNombre() {
        return nombre; }

    /**
     * Método para establecer el nombre del recepcionista
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    /**
     * Método para obtener el apellido del recepcionista
     * @return
     */
    public String getApellido() {
        return apellido; }

    /**
     * Método para establecer el apellido del recepcionista
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido; }

    /**
     * Método para obtener el turno del recepcionista
     * @return
     */
    public TurnoRecepcionista getTurno() {
        return turno; }

    /**
     * Método para establecer el turno del recepcionista
     * @param turno
     */
    public void setTurno(TurnoRecepcionista turno) {
        this.turno = turno; }
}
