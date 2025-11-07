package cl.huertohogar.usuario_backend.exception;

public class OrdenNotFoundException extends RuntimeException {
    public OrdenNotFoundException(String mensaje) {
        super(mensaje);
    }
}