package co.edu.uniquindio.sistemahotelmanager.model;

public class Administrador extends Usuario{

    private String idAdministrador;
    private String nombre;
    private String apellido;

    public Administrador(String idAdministrador, String nombre, String apellido,
                         String username, String password) {
        super(idAdministrador, username, password, Rol.ADMIN);
        this.idAdministrador = idAdministrador;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getIdAdministrador() { return idAdministrador; }
    public void setIdAdministrador(String id) { this.idAdministrador = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}
