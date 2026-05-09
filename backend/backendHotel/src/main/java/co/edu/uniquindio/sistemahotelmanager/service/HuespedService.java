package co.edu.uniquindio.sistemahotelmanager.service;

import co.edu.uniquindio.sistemahotelmanager.model.Huesped;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio que maneja la lógica de negocio relacionada con los huéspedes en el sistema de gestión hotelera.
 */
@Service
public class HuespedService {

    private final DataStore dataStore;

    public HuespedService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Registra un nuevo huésped en el sistema.
     * Valida que el username no esté repetido entre los huéspedes y el personal del hotel.
     * @param dto
     * @return
     */
    public Huesped registrarHuesped(HuespedRequestDTO dto) {
        validarDatosHuesped(dto);

        String usernameNormalizado = dto.getUsername().trim();
        boolean usernameExiste = dataStore.getHuespedes().stream()
                .anyMatch(h -> h.getUsername().equalsIgnoreCase(usernameNormalizado));
        if (usernameExiste) {
            throw new IllegalArgumentException(
                    "El username '" + usernameNormalizado + "' ya está registrado.");
        }

        boolean usernameEnPersonal = dataStore.getAdministradores().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(usernameNormalizado))
                || dataStore.getRecepcionistas().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(usernameNormalizado))
                || dataStore.getPersonalLimpieza().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(usernameNormalizado));
        if (usernameEnPersonal) {
            throw new IllegalArgumentException(
                    "El username '" + usernameNormalizado + "' ya está registrado como usuario del sistema.");
        }

        String idHuesped = "H-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Huesped huesped = new Huesped(idHuesped, dto.getNombre().trim(), dto.getApellido().trim(),
                dto.getTelefono().trim(), dto.getCorreo().trim(), usernameNormalizado, dto.getPassword());
        dataStore.getHuespedes().add(huesped);
        return huesped;
    }

    /**
     * Valida los datos del DTO de registro de huésped, asegurándose de que no estén vacíos y que la contraseña tenga al menos 4 caracteres.
     * @param dto
     */
    private void validarDatosHuesped(HuespedRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del huésped son obligatorios.");
        }
        if (estaVacio(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del huésped es obligatorio.");
        }
        if (estaVacio(dto.getApellido())) {
            throw new IllegalArgumentException("El apellido del huésped es obligatorio.");
        }
        if (estaVacio(dto.getTelefono())) {
            throw new IllegalArgumentException("El teléfono del huésped es obligatorio.");
        }
        if (estaVacio(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo del huésped es obligatorio.");
        }
        if (estaVacio(dto.getUsername())) {
            throw new IllegalArgumentException("El usuario del huésped es obligatorio.");
        }
        if (estaVacio(dto.getPassword()) || dto.getPassword().length() < 4) {
            throw new IllegalArgumentException("La contraseña del huésped debe tener mínimo 4 caracteres.");
        }
    }

    /**
     * Método auxiliar para verificar si un valor es nulo o está vacío después de eliminar espacios en blanco.
     * @param valor
     * @return
     */
    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /**
     * Lista todos los huéspedes registrados en el sistema.
     * @return
     */
    public List<Huesped> listarTodos() {
        return dataStore.getHuespedes();
    }

    /**
     * Busca un huésped por su ID, devolviendo un Optional que puede estar vacío si no se encuentra ningún huésped con el ID proporcionado.
     * @param idHuesped
     * @return
     */
    public Optional<Huesped> buscarPorId(String idHuesped) {
        return dataStore.getHuespedes().stream()
                .filter(h -> h.getIdHuesped().equals(idHuesped))
                .findFirst();
    }

    /**
     * Actualiza los datos de un huésped existente, buscando por su ID y modificando su teléfono y correo. Si no se encuentra el huésped, se lanza una excepción.
     * @param idHuesped
     * @param telefono
     * @param correo
     * @return
     */
    public Huesped actualizarDatos(String idHuesped, String telefono, String correo) {
        Huesped huesped = buscarPorId(idHuesped)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el huésped con ID: " + idHuesped));
        huesped.actualizarDatos(telefono, correo);
        return huesped;
    }

    /**
     * Elimina un huésped del sistema buscando por su ID.
     * Si no se encuentra el huésped, se lanza una excepción.
     * @param idHuesped
     */
    public void eliminar(String idHuesped) {
        Huesped huesped = buscarPorId(idHuesped)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el huésped con ID: " + idHuesped));
        dataStore.getHuespedes().remove(huesped);
    }
}

