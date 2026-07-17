package antonioschettini.u5_w3_d5.repositories;

import antonioschettini.u5_w3_d5.entities.Evento;
import antonioschettini.u5_w3_d5.entities.Prenotazione;
import antonioschettini.u5_w3_d5.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    // utility per contare quante prenotazioni attive ci sono per un evento
    long countByEvento(Evento evento);

    // utility per controllare se un utente ha prenotato già questo specifico evento
    boolean existsByUtenteAndEvento(User utente, Evento evento);

    // utility per trovare tutte le prenotazioni di un singolo utente
    List<Prenotazione> findByUtente(User utente);
}
