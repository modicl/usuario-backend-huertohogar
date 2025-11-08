package cl.huertohogar.usuario_backend.exception;

public class CiudadNotFoundException extends RuntimeException {
    public CiudadNotFoundException(String mensaje) {
        super(mensaje);
    }
}
