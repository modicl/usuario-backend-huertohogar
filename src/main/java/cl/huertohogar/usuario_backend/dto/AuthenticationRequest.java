package cl.huertohogar.usuario_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para autenticación de usuario")
public class AuthenticationRequest {

    @Schema(description = "Email del usuario", example = "luis@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "MiPassword123!")
    private String password;

}
