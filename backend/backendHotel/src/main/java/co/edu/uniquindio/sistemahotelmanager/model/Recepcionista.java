package co.edu.uniquindio.sistemahotelmanager.model;

public class Recepcionista  extends Usuario{

    private String idRecepcionista;
    private String nombre;
    private String apellido;
    private TurnoRecepcionista turno;

    public Recepcionista(String idRecepcionista, String nombre, String apellido,
                         TurnoRecepcionista turno, String username, String password) {
        super(idRecepcionista, username, password, Rol.RECEPCIONISTA);
        this.idRecepcionista = idRecepcionista;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getIdRecepcionista() { return idRecepcionista; }
    public void setIdRecepcionista(String id) { this.idRecepcionista = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public TurnoRecepcionista getTurno() { return turno; }
    public void setTurno(TurnoRecepcionista turno) { this.turno = turno; }
}
