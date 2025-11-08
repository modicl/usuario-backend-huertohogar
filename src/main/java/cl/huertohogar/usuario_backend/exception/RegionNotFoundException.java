package cl.huertohogar.usuario_backend.exception;

public class RegionNotFoundException extends RuntimeException {
    public RegionNotFoundException(String mensaje) {
        super(mensaje);
    }
}
