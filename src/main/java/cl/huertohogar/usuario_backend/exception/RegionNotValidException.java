package cl.huertohogar.usuario_backend.exception;

public class RegionNotValidException extends RuntimeException {
    public RegionNotValidException(String mensaje) {
        super(mensaje);
    }
}
