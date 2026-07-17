package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.entities.Prenotazione;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.recordDTO.NewPrenotazionePayload;
import antonioschettini.u5_w3_d5.services.PrenotazioniService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(
            @RequestBody @Validated NewPrenotazionePayload body,
            @AuthenticationPrincipal User currentUser
    ) {
        return prenotazioniService.save(body, currentUser);
    }

    // l'utente può vedere soltanto le sue prenotazioni
    @GetMapping("/me")
    public Page<Prenotazione> getMiePrenotazioni(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idPrenotazione") String sortBy
    ) {
        return prenotazioniService.findByUtente(currentUser.getIdUser(), page, size, sortBy);
    }

    @DeleteMapping("/{idPrenotazione}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable UUID idPrenotazione, @AuthenticationPrincipal User currentUser) {
        // Passo utente loggato per verificare che stia cancellando una prenotazione sua
        prenotazioniService.delete(idPrenotazione, currentUser);
    }
}