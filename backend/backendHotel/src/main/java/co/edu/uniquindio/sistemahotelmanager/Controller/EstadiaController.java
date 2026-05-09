package co.edu.uniquindio.sistemahotelmanager.Controller;

import co.edu.uniquindio.sistemahotelmanager.model.Estadia;
import co.edu.uniquindio.sistemahotelmanager.service.EstadiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadias")
@CrossOrigin(origins = "*")
public class EstadiaController {

    private final EstadiaService estadiaService;

    public EstadiaController(EstadiaService estadiaService) {
        this.estadiaService = estadiaService;
    }

    @GetMapping
    public ResponseEntity<List<Estadia>> listarTodas() {
        return ResponseEntity.ok(estadiaService.listarTodas());
    }

    @GetMapping("/para-pagos")
    public ResponseEntity<List<Estadia>> listarParaPagos() {
        return ResponseEntity.ok(estadiaService.listarParaPagos());
    }

    @GetMapping("/en-curso")
    public ResponseEntity<List<Estadia>> listarEnCurso() {
        return ResponseEntity.ok(estadiaService.listarEnCurso());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadia> buscarPorId(@PathVariable String id) {
        return estadiaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/checkin/{idReserva}")
    public ResponseEntity<Estadia> checkIn(@PathVariable String idReserva) {
        return ResponseEntity.ok(estadiaService.realizarCheckIn(idReserva));
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<Estadia> checkOut(@PathVariable String id) {
        return ResponseEntity.ok(estadiaService.realizarCheckOut(id));
    }

    @PostMapping("/consumo")
    public ResponseEntity<Estadia> agregarConsumo(@RequestBody ConsumoRequestDTO dto) {
        return ResponseEntity.ok(estadiaService.agregarConsumo(dto));
    }
}
