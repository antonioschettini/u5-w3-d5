package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.Prenotazione;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.exceptions.BadRequestException;
import antonioschettini.u5_w3_d5.exceptions.NotFoundException;
import antonioschettini.u5_w3_d5.recordDTO.NewPrenotazionePayload;
import antonioschettini.u5_w3_d5.repositories.PrenotazioneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PrenotazioniService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final UsersService usersService;
    private final EventiService eventiService;

    public PrenotazioniService(PrenotazioneRepository prenotazioneRepository, UsersService usersService, EventiService eventiService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.usersService = usersService;
        this.eventiService = eventiService;
    }

    // Crea una prenotazione eseguendo tutti i controlli richiesti
    public Prenotazione save(NewPrenotazionePayload body) {
        User utente = usersService.findById(body.idUtente());
        Evento evento = eventiService.findById(body.idEvento());

        // CONTROLLO 1: L'utente ha già prenotato questo specifico evento?
        if (prenotazioneRepository.existsByUtenteAndEvento(utente, evento)) {
            throw new BadRequestException("Hai già prenotato un posto per questo evento!");
        }

        // CONTROLLO 2: Ci sono ancora posti liberi?
        long postiOccupati = prenotazioneRepository.countByEvento(evento);
        if (postiOccupati >= evento.getPostiTotali()) {
            throw new BadRequestException("Spiacenti, i posti per l'evento '" + evento.getTitolo() + "' sono esauriti!");
        }

        Prenotazione nuovaPrenotazione = new Prenotazione();
        nuovaPrenotazione.setDataPrenotazione(LocalDate.now());
        nuovaPrenotazione.setUtente(utente);
        nuovaPrenotazione.setEvento(evento);

        return prenotazioneRepository.save(nuovaPrenotazione);
    }

    // Mostra tutte le prenotazioni fatte a sistema (paginate)
    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    // Mostra solo le prenotazioni di un determinato utente (paginate - per la funzione EXTRA)
    public Page<Prenotazione> findByUtente(UUID idUtente, int page, int size, String sortBy) {
        User utente = usersService.findById(idUtente);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findByUtente(utente, pageable);
    }

    // Annulla una prenotazione (cancella il biglietto)
    public void delete(UUID idPrenotazione) {
        Prenotazione trovata = prenotazioneRepository.findById(idPrenotazione)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata!"));
        prenotazioneRepository.delete(trovata);
    }
}