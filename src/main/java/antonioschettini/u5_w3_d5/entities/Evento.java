package antonioschettini.u5_w3_d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evento")
    @Setter(AccessLevel.NONE)
    private UUID idEvento;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, length = 800)
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String luogo;

    // posti totali per gestire la disponibilità di posti disponibili per l'evento
    @Column(name = "posti_totali", nullable = false)
    private int postiTotali;

    // relazione ManytoOne molti eventi possono essere creati dallo stesso organizzatore
    @ManyToOne
    @JoinColumn(name = "id_organizzatore", nullable = false)
    private User organizzatore;
}
