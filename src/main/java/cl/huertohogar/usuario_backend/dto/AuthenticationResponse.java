package cl.huertohogar.usuario_backend.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response de autenticación exitosa con token JWT y datos completos del usuario")
public class AuthenticationResponse {
    
    @Schema(description = "Token JWT para autenticar siguientes peticiones", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "ID del usuario autenticado", example = "1")
    private Integer idUsuario;
    
    @Schema(description = "Nombre del usuario", example = "Felipe")
    private String nombre;
    
    @Schema(description = "Segundo nombre del usuario", example = "Andrés")
    private String sNombre;
    
    @Schema(description = "Apellido paterno del usuario", example = "Villarroel")
    private String aPaterno;
    
    @Schema(description = "Apellido materno del usuario", example = "González")
    private String aMaterno;
    
    @Schema(description = "RUT del usuario", example = "12345678")
    private String rut;
    
    @Schema(description = "Dígito verificador del RUT", example = "9")
    private String dv;
    
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;
    
    @Schema(description = "ID de la región", example = "13")
    private Integer idRegion;
    
    @Schema(description = "Dirección del usuario", example = "Av. Libertador 123, Santiago")
    private String direccion;
    
    @Schema(description = "Email del usuario", example = "felipe.villarroel@gmail.com")
    private String email;
    
    @Schema(description = "Teléfono del usuario", example = "+56987654321")
    private String telefono;
    
    @Schema(description = "Rol del usuario", example = "USER", allowableValues = {"USER", "ADMIN"})
    private String rol;
}