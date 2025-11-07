package cl.huertohogar.usuario_backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    @Schema(description = "Identificador único del usuario")
    private Integer idUsuario;
    
    @Column(name = "pnombre", nullable = false)
    @Schema(description = "Primer nombre del usuario")
    private String pNombre;
    
    @Column(name = "snombre", nullable = false)
    @Schema(description = "Segundo nombre del usuario")
    private String sNombre;

    @Column(name= "apaterno", nullable = false)
    @Schema(description = "Apellido paterno del usuario")
    private String aPaterno;

    @Column(name= "amaterno", nullable = false)
    @Schema(description = "Apellido materno del usuario")
    private String aMaterno;

    @Column(name = "email", nullable = false)
    @Schema(description = "Correo electrónico del usuario")
    private String email;

    @Column(name = "telefono", nullable = false)
    @Schema(description = "Número de teléfono del usuario")
    private String telefono;

    @Column(name = "direccion", nullable = false)
    @Schema(description = "Dirección del usuario")
    private String direccion;

    @OneToOne
    @JoinColumn(name = "id_password", nullable = false)
    @Schema(description = "Contraseña asociada al usuario")
    private Password password;  
}
