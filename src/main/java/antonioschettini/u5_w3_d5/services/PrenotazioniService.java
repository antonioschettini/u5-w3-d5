package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.Prenotazione;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.exceptions.BadRequestException;
import antonioschettini.u5_w3_d5.exceptions.ForbiddenException;
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

    // creo una prenotazione
    public Prenotazione save(NewPrenotazionePayload body, User currentUser) {
        // uso il currentuser per verificare il token
        Evento evento = eventiService.findById(body.idEvento());
        // se ho già effettuato la prenotazione lancio l'eccezione
        if (prenotazioneRepository.existsByUtenteAndEvento(currentUser, evento)) {
            throw new BadRequestException("Hai già prenotato un posto per questo evento!");
        }
        // se i posti sono esauriti lancio eccezione
        long postiOccupati = prenotazioneRepository.countByEvento(evento);
        if (postiOccupati >= evento.getPostiTotali()) {
            throw new BadRequestException("Spiacenti, i posti per l'evento '" + evento.getTitolo() + "' sono esauriti!");
        }

        Prenotazione nuovaPrenotazione = new Prenotazione();
        nuovaPrenotazione.setDataPrenotazione(LocalDate.now());
        nuovaPrenotazione.setUtente(currentUser);
        nuovaPrenotazione.setEvento(evento);

        return prenotazioneRepository.save(nuovaPrenotazione);
    }

    // get di tutte le prenotazioni
    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    // get di tutte le prenotazioni fatte da un utente
    public Page<Prenotazione> findByUtente(UUID idUtente, int page, int size, String sortBy) {
        User utente = usersService.findById(idUtente);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findByUtente(utente, pageable);
    }

    // cancella una prenotazione
    public void delete(UUID idPrenotazione, User currentUser) {
        Prenotazione trovata = prenotazioneRepository.findById(idPrenotazione)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata!"));

        // un utente può cancellare la prenotazione solo se è la sua
        if (!trovata.getUtente().getIdUser().equals(currentUser.getIdUser())) {
            throw new ForbiddenException("Non sei autorizzato a cancellare una prenotazione di un altro utente.");
        }

        prenotazioneRepository.delete(trovata);
    }
}