package co.edu.uniquindio.sistemahotelmanager.Controller;

import co.edu.uniquindio.sistemahotelmanager.data.DataStore;
import co.edu.uniquindio.sistemahotelmanager.dto.PersonalLimpiezaRequestDTO;
import co.edu.uniquindio.sistemahotelmanager.dto.RecepcionistaRequestDTO;
import co.edu.uniquindio.sistemahotelmanager.dto.UsuarioResponseDTO;
import co.edu.uniquindio.sistemahotelmanager.model.Administrador;
import co.edu.uniquindio.sistemahotelmanager.model.PersonalLimpieza;
import co.edu.uniquindio.sistemahotelmanager.model.Recepcionista;
import co.edu.uniquindio.sistemahotelmanager.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final DataStore dataStore;

    /**
     * Constructor para inyectar el DataStore.
     * @param dataStore
     */
    public UsuarioController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Endpoint para listar todos los usuarios del sistema (administradores, recepcionistas y personal de limpieza).
     * @return
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = new ArrayList<>();
        dataStore.getAdministradores().forEach(admin -> usuarios.add(toResponse(admin)));
        dataStore.getRecepcionistas().forEach(recepcionista -> usuarios.add(toResponse(recepcionista)));
        dataStore.getPersonalLimpieza().forEach(personal -> usuarios.add(toResponse(personal)));
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Endpoint para crear un nuevo recepcionista. Solo el administrador autenticado puede realizar esta acción.
     * @param rol
     * @param idUsuario
     * @param username
     * @param dto
     * @return
     */
    @PostMapping("/recepcionistas")
    public ResponseEntity<UsuarioResponseDTO> crearRecepcionista(@RequestHeader(value = "X-User-Role", required = false) String rol,
                                                                 @RequestHeader(value = "X-User-Id", required = false) String idUsuario,
                                                                 @RequestHeader(value = "X-Username", required = false) String username,
                                                                 @RequestBody RecepcionistaRequestDTO dto) {
        validarAdminAutenticado(rol, idUsuario, username);
        validarRecepcionista(dto);
        validarUsernameUnico(dto.getUsername());

        String idRecepcionista = generarIdRecepcionista();
        Recepcionista recepcionista = new Recepcionista(
                idRecepcionista,
                dto.getNombre().trim(),
                dto.getApellido().trim(),
                dto.getTurno(),
                dto.getUsername().trim(),
                dto.getPassword()
        );

        dataStore.getRecepcionistas().add(recepcionista);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(recepcionista));
    }

    /**
     * Endpoint para crear un nuevo personal de limpieza. Solo el administrador autenticado puede realizar esta acción.
     * @param rol
     * @param idUsuario
     * @param username
     * @param dto
     * @return
     */
    @PostMapping("/personal-limpieza")
    public ResponseEntity<UsuarioResponseDTO> crearPersonalLimpieza(@RequestHeader(value = "X-User-Role", required = false) String rol,
                                                                    @RequestHeader(value = "X-User-Id", required = false) String idUsuario,
                                                                    @RequestHeader(value = "X-Username", required = false) String username,
                                                                    @RequestBody PersonalLimpiezaRequestDTO dto) {
        validarAdminAutenticado(rol, idUsuario, username);
        validarPersonalLimpieza(dto);
        validarUsernameUnico(dto.getUsername());

        String idPersonal = generarIdPersonalLimpieza();
        PersonalLimpieza personal = new PersonalLimpieza(
                idPersonal,
                dto.getNombre().trim(),
                dto.getApellido().trim(),
                dto.getUsername().trim(),
                dto.getPassword()
        );

        dataStore.getPersonalLimpieza().add(personal);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(personal));
    }

    /**
     * Endpoint para cambiar el estado (activo/inactivo) de un usuario (administrador, recepcionista o personal de limpieza). Solo el administrador autenticado puede realizar esta acción.
     * @param rol
     * @param idUsuario
     * @param username
     * @param id
     * @return
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(@RequestHeader(value = "X-User-Role", required = false) String rol,
                                                            @RequestHeader(value = "X-User-Id", required = false) String idUsuario,
                                                            @RequestHeader(value = "X-Username", required = false) String username,
                                                            @PathVariable String id) {
        validarAdminAutenticado(rol, idUsuario, username);
        for (Administrador admin : dataStore.getAdministradores()) {
            if (admin.getIdAdministrador().equals(id)) {
                admin.setActivo(!admin.isActivo());
                return ResponseEntity.ok(toResponse(admin));
            }
        }

        for (Recepcionista recepcionista : dataStore.getRecepcionistas()) {
            if (recepcionista.getIdRecepcionista().equals(id)) {
                recepcionista.setActivo(!recepcionista.isActivo());
                return ResponseEntity.ok(toResponse(recepcionista));
            }
        }

        for (PersonalLimpieza personal : dataStore.getPersonalLimpieza()) {
            if (personal.getIdPersonalLimpieza().equals(id)) {
                personal.setActivo(!personal.isActivo());
                return ResponseEntity.ok(toResponse(personal));
            }
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Método auxiliar para validar que el usuario autenticado es un administrador activo. Si no se cumple esta condición, se lanza una excepción indicando que solo el administrador puede realizar la acción solicitada.
     * @param rol
     * @param idUsuario
     * @param username
     */
    private void validarAdminAutenticado(String rol, String idUsuario, String username) {
        boolean rolAdmin = esRolAdmin(rol);
        boolean adminExiste = dataStore.getAdministradores().stream()
                .anyMatch(admin -> admin.isActivo()
                        && coincide(admin.getIdAdministrador(), idUsuario)
                        && coincide(admin.getUsername(), username));

        if (!rolAdmin || !adminExiste) {
            throw new IllegalStateException("Solo el administrador puede crear o administrar personal del hotel.");
        }
    }

    /**
     * Método auxiliar para verificar si el rol proporcionado corresponde a un administrador. Se normaliza el valor del rol eliminando espacios en blanco y convirtiéndolo a mayúsculas para permitir diferentes formatos de entrada (por ejemplo, "admin", "ADMIN", "Role_Admin", etc.). Si el rol es nulo o no coincide con ninguna de las variantes reconocidas, se devuelve false.
     * @param rol
     * @return
     */
    private boolean esRolAdmin(String rol) {
        if (rol == null) {
            return false;
        }
        String rolNormalizado = rol.trim().toUpperCase();
        return rolNormalizado.equals("ADMIN")
                || rolNormalizado.equals("ROLE_ADMIN")
                || rolNormalizado.equals("ADMINISTRADOR");
    }

    /**
     * Método auxiliar para comparar dos cadenas de texto, ignorando mayúsculas y espacios en blanco al inicio o al final. Devuelve true si ambas cadenas son iguales después de normalizarlas, o false si alguna de las cadenas es nula o si no coinciden.
     * @param esperado
     * @param recibido
     * @return
     */
    private boolean coincide(String esperado, String recibido) {
        return esperado != null && recibido != null && esperado.equalsIgnoreCase(recibido.trim());
    }

    /**
     * Método auxiliar para validar los datos del DTO de creación de recepcionista. Verifica que el DTO no sea nulo, que los campos básicos (nombre, apellido, username y password) no estén vacíos y que la contraseña tenga al menos 4 caracteres. Además, verifica que el turno del recepcionista no sea nulo. Si alguna de estas condiciones no se cumple, se lanza una excepción IllegalArgumentException con un mensaje descriptivo del error.
     * @param dto
     */
    private void validarRecepcionista(RecepcionistaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del recepcionista son obligatorios.");
        }
        validarDatosBasicos(dto.getNombre(), dto.getApellido(), dto.getUsername(), dto.getPassword(), "recepcionista");
        if (dto.getTurno() == null) {
            throw new IllegalArgumentException("El turno del recepcionista es obligatorio.");
        }
    }

    /**
     * Método auxiliar para validar los datos del DTO de creación de personal de limpieza. Verifica que el DTO no sea nulo, que los campos básicos (nombre, apellido, username y password) no estén vacíos y que la contraseña tenga al menos 4 caracteres. Si alguna de estas condiciones no se cumple, se lanza una excepción IllegalArgumentException con un mensaje descriptivo del error.
     * @param dto
     */
    private void validarPersonalLimpieza(PersonalLimpiezaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del personal de limpieza son obligatorios.");
        }
        validarDatosBasicos(dto.getNombre(), dto.getApellido(), dto.getUsername(), dto.getPassword(), "personal de limpieza");
    }

    /**
     * Método auxiliar para validar los datos básicos comunes a la creación de recepcionistas y personal de limpieza. Verifica que el nombre, apellido, username y password no estén vacíos, y que la contraseña tenga al menos 4 caracteres. Si alguna de estas condiciones no se cumple, se lanza una excepción IllegalArgumentException con un mensaje descriptivo del error, indicando el tipo de usuario (recepcionista o personal de limpieza) para contextualizar el mensaje.
     * @param nombre
     * @param apellido
     * @param username
     * @param password
     * @param tipoUsuario
     */
    private void validarDatosBasicos(String nombre, String apellido, String username, String password, String tipoUsuario) {
        if (estaVacio(nombre)) {
            throw new IllegalArgumentException("El nombre del " + tipoUsuario + " es obligatorio.");
        }
        if (estaVacio(apellido)) {
            throw new IllegalArgumentException("El apellido del " + tipoUsuario + " es obligatorio.");
        }
        if (estaVacio(username)) {
            throw new IllegalArgumentException("El usuario de acceso es obligatorio.");
        }
        if (estaVacio(password) || password.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 4 caracteres.");
        }
    }

    /**
     * Método auxiliar para validar que el username proporcionado no esté registrado en el sistema, verificando tanto en la lista de administradores, recepcionistas, personal de limpieza y huéspedes. Se normaliza el username eliminando espacios en blanco al inicio y al final, y se compara de forma insensible a mayúsculas para evitar problemas de formato. Si el username ya existe en alguna de las listas, se lanza una excepción IllegalArgumentException indicando que el username ya está registrado.
     * @param username
     */
    private void validarUsernameUnico(String username) {
        String usernameNormalizado = username.trim();

        boolean existeAdmin = dataStore.getAdministradores().stream()
                .map(Usuario::getUsername)
                .anyMatch(u -> u.equalsIgnoreCase(usernameNormalizado));

        boolean existeRecepcionista = dataStore.getRecepcionistas().stream()
                .map(Usuario::getUsername)
                .anyMatch(u -> u.equalsIgnoreCase(usernameNormalizado));

        boolean existePersonalLimpieza = dataStore.getPersonalLimpieza().stream()
                .map(Usuario::getUsername)
                .anyMatch(u -> u.equalsIgnoreCase(usernameNormalizado));

        boolean existeHuesped = dataStore.getHuespedes().stream()
                .map(Usuario::getUsername)
                .anyMatch(u -> u.equalsIgnoreCase(usernameNormalizado));

        if (existeAdmin || existeRecepcionista || existePersonalLimpieza || existeHuesped) {
            throw new IllegalArgumentException("El username '" + usernameNormalizado + "' ya está registrado.");
        }
    }

    /**
     * Método auxiliar para generar un ID único para un nuevo recepcionista. El formato del ID es "R-XXX", donde XXX es un número consecutivo de tres dígitos. El método comienza con el número de usuarios existentes en la lista de recepcionistas más uno, y genera un ID incrementando el número hasta encontrar uno que no exista en la lista. Para verificar la existencia del ID, se utiliza el método existeRecepcionistaConId, que recorre la lista de recepcionistas y compara el ID generado con los IDs existentes. De esta manera, se garantiza que cada nuevo recepcionista tenga un ID único en el sistema.
     * @return
     */
    private String generarIdRecepcionista() {
        int consecutivo = dataStore.getRecepcionistas().size() + 1;
        String id;
        do {
            id = String.format("R-%03d", consecutivo++);
        } while (existeRecepcionistaConId(id));
        return id;
    }

    /**
     * Método auxiliar para generar un ID único para un nuevo personal de limpieza. El formato del ID es "PL-XXX", donde XXX es un número consecutivo de tres dígitos. El método comienza con el número de usuarios existentes en la lista de personal de limpieza más uno, y genera un ID incrementando el número hasta encontrar uno que no exista en la lista. Para verificar la existencia del ID, se utiliza el método existePersonalLimpiezaConId, que recorre la lista de personal de limpieza y compara el ID generado con los IDs existentes. De esta manera, se garantiza que cada nuevo personal de limpieza tenga un ID único en el sistema.
     * @return
     */
    private String generarIdPersonalLimpieza() {
        int consecutivo = dataStore.getPersonalLimpieza().size() + 1;
        String id;
        do {
            id = String.format("PL-%03d", consecutivo++);
        } while (existePersonalLimpiezaConId(id));
        return id;
    }

    /**
     * Método auxiliar para verificar si ya existe un recepcionista con el ID proporcionado. Recorre la lista de recepcionistas en el DataStore y compara el ID generado con los IDs existentes utilizando el método anyMatch. Si encuentra un recepcionista con el mismo ID, devuelve true; de lo contrario, devuelve false. Este método se utiliza para garantizar que cada nuevo recepcionista tenga un ID único en el sistema.
     * @param id
     * @return
     */
    private boolean existeRecepcionistaConId(String id) {
        return dataStore.getRecepcionistas().stream()
                .anyMatch(r -> r.getIdRecepcionista().equals(id));
    }

    /**
     * Método auxiliar para verificar si ya existe un personal de limpieza con el ID proporcionado. Recorre la lista de personal de limpieza en el DataStore y compara el ID generado con los IDs existentes utilizando el método anyMatch. Si encuentra un personal de limpieza con el mismo ID, devuelve true; de lo contrario, devuelve false. Este método se utiliza para garantizar que cada nuevo personal de limpieza tenga un ID único en el sistema.
     * @param id
     * @return
     */
    private boolean existePersonalLimpiezaConId(String id) {
        return dataStore.getPersonalLimpieza().stream()
                .anyMatch(p -> p.getIdPersonalLimpieza().equals(id));
    }

    /**
     * Método auxiliar para verificar si un valor es nulo o está vacío después de eliminar espacios en blanco. Devuelve true si el valor es nulo o si, después de eliminar los espacios en blanco al inicio y al final, la cadena resultante está vacía. De lo contrario, devuelve false. Este método se utiliza para validar que los campos obligatorios no estén vacíos al crear nuevos usuarios.
     * @param valor
     * @return
     */
    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /**
     * Métodos auxiliares para convertir entidades de usuario (Administrador, Recepcionista, PersonalLimpieza) a DTOs de respuesta (UsuarioResponseDTO). Estos métodos extraen los campos relevantes de cada tipo de usuario y los mapean a un DTO común que se utiliza para enviar la información al cliente. Esto permite una representación uniforme de los usuarios en las respuestas de la API, independientemente de su tipo específico.
     * @param admin
     * @return
     */
    private UsuarioResponseDTO toResponse(Administrador admin) {
        return new UsuarioResponseDTO(admin.getIdAdministrador(), admin.getUsername(), admin.getNombre(), admin.getApellido(), admin.getRol(), admin.isActivo());
    }

    private UsuarioResponseDTO toResponse(Recepcionista recepcionista) {
        return new UsuarioResponseDTO(recepcionista.getIdRecepcionista(), recepcionista.getUsername(), recepcionista.getNombre(), recepcionista.getApellido(), recepcionista.getRol(), recepcionista.isActivo());
    }

    private UsuarioResponseDTO toResponse(PersonalLimpieza personal) {
        return new UsuarioResponseDTO(personal.getIdPersonalLimpieza(), personal.getUsername(), personal.getNombre(), personal.getApellido(), personal.getRol(), personal.isActivo());
    }
}
