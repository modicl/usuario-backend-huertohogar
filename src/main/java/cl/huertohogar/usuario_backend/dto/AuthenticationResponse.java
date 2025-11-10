package cl.huertohogar.usuario_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer idUsuario;
    private String email;
    private String pNombre;
    private String aPaterno;
}