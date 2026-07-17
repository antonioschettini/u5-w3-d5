package antonioschettini.u5_w3_d5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);

    }

    public NotFoundException(UUID id) {
        super("Elemento con ID " + id + " non è stato trovato!");
    }
}
