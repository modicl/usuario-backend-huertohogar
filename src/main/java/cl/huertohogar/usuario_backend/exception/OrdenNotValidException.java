package cl.huertohogar.usuario_backend.exception;

public class OrdenNotValidException extends RuntimeException {
    public OrdenNotValidException(String mensaje) {
        super(mensaje);
    }
}