package antonioschettini.u5_w3_d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_prenotazione")
    @Setter(AccessLevel.NONE)
    private UUID idPrenotazione;

    @Column(name = "data_prenotazione", nullable = false)
    private LocalDate dataPrenotazione;

    // relazione ManytoOne molte prenotazioni possono appartenere allo stesso utente
    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private User utente;

    // relazione ManyToOne molte prenotazioni possono essere fatte per lo stesso evento
    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;
}
