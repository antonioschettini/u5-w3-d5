package antonioschettini.u5_w3_d5.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsPayload(
        String messaggio,
        LocalDateTime timestamp,
        List<String> listaErrori
) {
}
