package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.enums.Ruolo;
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
    private final UsersService usersService;

    public EventiService(EventoRepository eventoRepository, UsersService usersService) {
        this.eventoRepository = eventoRepository;
        this.usersService = usersService;
    }

    // creo un nuovo evento collegandolo all'organizzatore ed è possibile crearlo solo se sei un organizzatore
    public Evento save(NewEventoPayload body, UUID idOrganizzatore) {
        User organizzatore = usersService.findById(idOrganizzatore);

        // solo se sei un organizzatore puoi procedere
        if (!organizzatore.getRuolo().equals(Ruolo.ORGANIZZATORE)) {
            throw new ForbiddenException("Accesso negato! Solo gli organizzatori possono creare eventi.");
        }

        Evento nuovoEvento = new Evento();
        nuovoEvento.setTitolo(body.titolo());
        nuovoEvento.setDescrizione(body.descrizione());
        nuovoEvento.setData(body.data());
        nuovoEvento.setLuogo(body.luogo());
        nuovoEvento.setPostiTotali(body.postiTotali());
        nuovoEvento.setOrganizzatore(organizzatore);

        return eventoRepository.save(nuovoEvento);
    }

    // lista di tutti gli eventi
    public Page<Evento> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventoRepository.findAll(pageable);
    }

    // cerca un evento per id
    public Evento findById(UUID idEvento) {
        return eventoRepository.findById(idEvento)
                .orElseThrow(() -> new NotFoundException("Evento con ID " + idEvento + " non trovato!"));
    }

    // modifica un evento esistente
    public Evento update(UUID idEvento, NewEventoPayload body) {
        Evento trovato = this.findById(idEvento);
        trovato.setTitolo(body.titolo());
        trovato.setDescrizione(body.descrizione());
        trovato.setData(body.data());
        trovato.setLuogo(body.luogo());
        trovato.setPostiTotali(body.postiTotali());
        return eventoRepository.save(trovato);
    }

    // cancello un evento
    public void delete(UUID idEvento) {
        Evento trovato = this.findById(idEvento);
        eventoRepository.delete(trovato);
    }
}
