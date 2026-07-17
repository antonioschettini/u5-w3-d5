package antonioschettini.u5_w3_d5.repositories;

import antonioschettini.u5_w3_d5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
