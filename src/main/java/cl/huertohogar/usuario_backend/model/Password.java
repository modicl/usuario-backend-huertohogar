package cl.huertohogar.usuario_backend.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una contraseña")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_password", nullable = false)
    @Schema(description = "Identificador único de la contraseña")
    private Integer idPassword;

    @Column(name = "id_usuario", nullable = false)
    @Schema(description = "Identificador del usuario al que pertenece la contraseña")
    private Integer idUsuario;

    @Column(name = "password", nullable = false)
    @Schema(description = "Contraseña del usuario")
    private String password;

}
