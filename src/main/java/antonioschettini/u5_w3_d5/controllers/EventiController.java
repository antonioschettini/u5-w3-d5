package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.recordDTO.NewEventoPayload;
import antonioschettini.u5_w3_d5.services.EventiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventiController {

    private final EventiService eventiService;

    public EventiController(EventiService eventiService) {
        this.eventiService = eventiService;
    }

    // post per un nuovo evento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evento createEvento(
            @RequestBody @Validated NewEventoPayload body,
            @RequestParam UUID idOrganizzatore
    ) {
        return eventiService.save(body, idOrganizzatore);
    }

    // get all
    @GetMapping
    public Page<Evento> getAllEventi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return eventiService.findAll(page, size, sortBy);
    }

    // get by id
    @GetMapping("/{idEvento}")
    public Evento getEventoById(@PathVariable UUID idEvento) {
        return eventiService.findById(idEvento);
    }

    // put per modifica
    @PutMapping("/{idEvento}")
    public Evento updateEvento(@PathVariable UUID idEvento, @RequestBody @Validated NewEventoPayload body) {
        return eventiService.update(idEvento, body);
    }

    // delete
    @DeleteMapping("/{idEvento}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Risponde con un 204 fisso (Vuoto, ma andato a buon fine)
    public void deleteEvento(@PathVariable UUID idEvento) {
        eventiService.delete(idEvento);
    }
}