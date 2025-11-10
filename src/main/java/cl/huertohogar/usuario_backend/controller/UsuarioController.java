package cl.huertohogar.usuario_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.huertohogar.usuario_backend.dto.AuthenticationRequest;
import cl.huertohogar.usuario_backend.dto.AuthenticationResponse;
import cl.huertohogar.usuario_backend.dto.PasswordUpdateRequest;
import cl.huertohogar.usuario_backend.dto.PasswordResetRequest;
import cl.huertohogar.usuario_backend.dto.PasswordValidationRequest;
import cl.huertohogar.usuario_backend.exception.AuthenticationFailedException;
import cl.huertohogar.usuario_backend.exception.UsuarioNotFoundException;
import cl.huertohogar.usuario_backend.model.Usuario;
import cl.huertohogar.usuario_backend.service.UsuarioService;
import cl.huertohogar.usuario_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "API Usuario", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario en el sistema con todos sus datos requeridos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos ingresados NO válidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-01T10:30:00\",\"message\":\"El primer nombre del usuario es obligatorio\",\"status\":400}")
            )
        )
    })
    @PostMapping("")
    public ResponseEntity<Usuario> createUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Ingrese datos del usuario a crear",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = Usuario.class),
                    examples = @ExampleObject(
                        name = "Ejemplo de producto",
                        value = "{\"idUsuario\":0,\"primerNombre\":\"Luis\",\"segundoNombre\":\"Andrés\",\"primerApellido\":\"González\",\"segundoApellido\":\"Ramírez\",\"email\":\"luisgonzalez@gmail.com\",\"telefono\":\"+56987654321\",\"direccion\":\"Calle Prueba 123\",\"password\":{\"idPassword\":0,\"valorPassword\":\"MiPassword123!\"}}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Usuario usuario) { 
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(
        summary = "Listar todos los usuarios",
        description = "Obtiene la lista completa de usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron usuarios",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-01T10:30:00\",\"message\":\"No se encontraron usuarios\",\"status\":404}")
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Busca y retorna un usuario por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-01T10:30:00\",\"message\":\"Usuario no encontrado con id: 999\",\"status\":404}")
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(
        summary = "Actualizar usuario completo",
        description = "Actualiza todos los campos de un usuario existente. Requiere enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos completos del usuario actualizado",
                content = @Content(
                    schema = @Schema(implementation = Usuario.class),
                    examples = @ExampleObject(
                        value = "{\"idUsuario\":1,\"primerNombre\":\"Luis\",\"segundoNombre\":\"Andrés\",\"primerApellido\":\"González\",\"segundoApellido\":\"Ramírez\",\"email\":\"luisgonzalez@gmail.com\",\"telefono\":\"+56987654321\",\"direccion\":\"Calle Prueba 123\",\"password\":{\"idPassword\":1,\"valorPassword\":\"MiPassword123!\"}}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
            Usuario usuarioActualizado = usuarioService.update(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
    }

    @Operation(
        summary = "Actualizar usuario parcialmente",
        description = "Actualiza solo los campos especificados de un usuario. No es necesario enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado parcialmente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> partialUpdateUsuario(
            @Parameter(description = "ID del Usuario", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Campos a actualizar (solo los que se envíen serán modificados)",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Actualizar email",
                        value = "{\"email\":\"luisitocomunica@gmail.com\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
            Usuario usuarioActualizado = usuarioService.partialUpdate(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina permanentemente un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Buscar usuarios por apellidos paternos",
        description = "Obtiene todos los usuarios que pertenecen a una categoría específica, como apellido paterno"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron usuarios para esa categoría",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Usuario>> getProductosPorCategoria(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.findByAPaterno(id));
    }

    // ==================== ENDPOINTS DE AUTENTICACIÓN Y CONTRASEÑA ====================

    @Operation(
        summary = "Autenticar usuario",
        description = "Valida las credenciales de un usuario verificando su contraseña"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @org.springframework.web.bind.annotation.RequestBody AuthenticationRequest request) {
        try {
            Usuario usuario = usuarioService.authenticateByEmailOrThrow(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(usuario.getIdUsuario(), usuario.getEmail());
            
            AuthenticationResponse response = new AuthenticationResponse(
                token,
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getPNombre(),
                usuario.getAPaterno()
            );
            return ResponseEntity.ok(response);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @Operation(
        summary = "Cambiar contraseña",
        description = "Cambia la contraseña de un usuario. Requiere la contraseña anterior para validación."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o contraseña anterior incorrecta",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Contraseña anterior incorrecta",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<String> changePassword(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Contraseñas antigua y nueva",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = PasswordUpdateRequest.class),
                    examples = @ExampleObject(
                        value = "{\"oldPassword\":\"OldPassword123!\",\"newPassword\":\"NewPassword456!\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody PasswordUpdateRequest request) {
        usuarioService.changePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }

    @Operation(
        summary = "Resetear contraseña",
        description = "Resetea la contraseña sin validar la anterior (solo para administradores)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña reseteada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Nueva contraseña inválida")
    })
    @PatchMapping("/{id}/resetear-contrasena")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nueva contraseña",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = PasswordResetRequest.class),
                    examples = @ExampleObject(
                        value = "{\"newPassword\":\"ResetPassword123!\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody PasswordResetRequest request) {
        usuarioService.resetPassword(id, request.getNewPassword());
        return ResponseEntity.ok("Contraseña reseteada exitosamente");
    }

    @Operation(
        summary = "Validar formato de contraseña",
        description = "Verifica si una contraseña cumple con los requisitos de seguridad"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación completada")
    })
    @PostMapping("/validar-contrasena")
    public ResponseEntity<String> validatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Contraseña a validar",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = PasswordValidationRequest.class),
                    examples = @ExampleObject(
                        value = "{\"password\":\"TestPassword123!\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody PasswordValidationRequest request) {
        boolean isValid = usuarioService.isValidPassword(request.getPassword());
        String strength = usuarioService.getPasswordStrength(request.getPassword());
        return ResponseEntity.ok("Válida: " + isValid + ", Fortaleza: " + strength);
    }

}
