package co.edu.uniquindio.sistemahotelmanager.model;

public class PersonalLimpieza extends Usuario{

    private String idPersonalLimpieza;
    private String nombre;
    private String apellido;

    public PersonalLimpieza(String idPersonalLimpieza, String nombre, String apellido,
                            String username, String password) {
        super(idPersonalLimpieza, username, password, Rol.PERSONAL_LIMPIEZA);
        this.idPersonalLimpieza = idPersonalLimpieza;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getIdPersonalLimpieza() { return idPersonalLimpieza; }
    public void setIdPersonalLimpieza(String id) { this.idPersonalLimpieza = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}
