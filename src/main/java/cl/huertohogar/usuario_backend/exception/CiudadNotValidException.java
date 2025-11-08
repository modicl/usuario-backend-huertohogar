package cl.huertohogar.usuario_backend.exception;

public class CiudadNotValidException extends RuntimeException {
    public CiudadNotValidException(String mensaje) {
        super(mensaje);
    }
}
