package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.recordDTO.NewEventoPayload;
import antonioschettini.u5_w3_d5.services.EventiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // solo chi è organizzatore può creare un evento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento createEvento(
            @RequestBody @Validated NewEventoPayload body,
            @AuthenticationPrincipal User currentUser
    ) {
        return eventiService.save(body, currentUser);
    }

    // tutti gli utenti loggati possono vedere la lista degli utenti
    @GetMapping
    public Page<Evento> getAllEventi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idEvento") String sortBy
    ) {
        return eventiService.findAll(page, size, sortBy);
    }

    // tutti gli utenti loggati possono vedere il singolo evento
    @GetMapping("/{idEvento}")
    public Evento getEventoById(@PathVariable UUID idEvento) {
        return eventiService.findById(idEvento);
    }

    // solo un organizzatore può modificare un evento
    @PutMapping("/{idEvento}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento updateEvento(
            @PathVariable UUID idEvento,
            @RequestBody @Validated NewEventoPayload body,
            @AuthenticationPrincipal User currentUser
    ) {
        return eventiService.update(idEvento, body, currentUser);
    }

    // solo un organizzatore può eliminare un evento
    @DeleteMapping("/{idEvento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void deleteEvento(@PathVariable UUID idEvento, @AuthenticationPrincipal User currentUser) {
        eventiService.delete(idEvento, currentUser);
    }
}