package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.entities.Prenotazione;
import antonioschettini.u5_w3_d5.recordDTO.NewPrenotazionePayload;
import antonioschettini.u5_w3_d5.services.PrenotazioniService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // post per una nuova prenotazione
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody @Validated NewPrenotazionePayload body) {
        return prenotazioniService.save(body);
    }

    // get all
    @GetMapping
    public Page<Prenotazione> getAllPrenotazioni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return prenotazioniService.findAll(page, size, sortBy);
    }

    // get all di tutte le prenotazioni fatte da un singolo utente
    @GetMapping("/utente/{idUtente}")
    public Page<Prenotazione> getPrenotazioniByUtente(
            @PathVariable UUID idUtente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return prenotazioniService.findByUtente(idUtente, page, size, sortBy);
    }

    // delete
    @DeleteMapping("/{idPrenotazione}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable UUID idPrenotazione) {
        prenotazioniService.delete(idPrenotazione);
    }
}