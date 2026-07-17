package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.exceptions.ForbiddenException;
import antonioschettini.u5_w3_d5.exceptions.NotFoundException;
import antonioschettini.u5_w3_d5.recordDTO.NewEventoPayload;
import antonioschettini.u5_w3_d5.repositories.EventoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventiService {
    private final EventoRepository eventoRepository;

    public EventiService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    //uso l'utente estratto dal token una volta loggato
    public Evento save(NewEventoPayload body, User organizzatore) {
        Evento nuovoEvento = new Evento();
        nuovoEvento.setTitolo(body.titolo());
        nuovoEvento.setDescrizione(body.descrizione());
        nuovoEvento.setData(body.data());
        nuovoEvento.setLuogo(body.luogo());
        nuovoEvento.setPostiTotali(body.postiTotali());
        nuovoEvento.setOrganizzatore(organizzatore);

        return eventoRepository.save(nuovoEvento);
    }

    public Page<Evento> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID idEvento) {
        return eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NotFoundException("Evento con ID " + idEvento + " non trovato!"));
    }

    // update con verifica se l'utente loggato è il proprietario dell'evento
    public Evento update(UUID idEvento, NewEventoPayload body, User currentUser) {
        Evento trovato = this.findById(idEvento);

        if (!trovato.getOrganizzatore().getIdUser().equals(currentUser.getIdUser())) {
            throw new ForbiddenException("Accesso negato! Non puoi modificare un evento creato da un altro organizzatore.");
        }

        trovato.setTitolo(body.titolo());
        trovato.setDescrizione(body.descrizione());
        trovato.setData(body.data());
        trovato.setLuogo(body.luogo());
        trovato.setPostiTotali(body.postiTotali());
        return eventoRepository.save(trovato);
    }

    // delete con controllo se l'utente è il proprietario dell'evento
    public void delete(UUID idEvento, User currentUser) {
        Evento trovato = this.findById(idEvento);

        if (!trovato.getOrganizzatore().getIdUser().equals(currentUser.getIdUser())) {
            throw new ForbiddenException("Accesso negato! Non puoi eliminare un evento creato da un altro organizzatore.");
        }

        eventoRepository.delete(trovato);
    }
}