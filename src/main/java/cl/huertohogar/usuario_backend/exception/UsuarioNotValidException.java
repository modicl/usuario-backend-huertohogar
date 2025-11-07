package cl.huertohogar.usuario_backend.exception;

public class UsuarioNotValidException extends RuntimeException {
    public UsuarioNotValidException(String mensaje) {
        super(mensaje);
    }
}