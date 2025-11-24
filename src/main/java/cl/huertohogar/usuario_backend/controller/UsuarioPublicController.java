package cl.huertohogar.usuario_backend.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.huertohogar.usuario_backend.model.Usuario;
import cl.huertohogar.usuario_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


// Endpoint publico que nos permite obtener informacion no sensible del usuario (para comentarios)
@RestController
@RequestMapping("/api/v1/public/usuarios")
@Tag(name = "API Usuario Pública", description = "Operaciones públicas relacionadas con los usuarios")
public class UsuarioPublicController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Obtener primer nombre del usuario", 
        description = "Retorna solo el primer nombre del usuario por su ID. Endpoint público."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nombre encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}/nombre")
    public ResponseEntity<Map<String, String>> getUsuarioNombre(
            @Parameter(description = "ID del usuario", example = "1") 
            @PathVariable Integer id) {
        
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(Collections.singletonMap("nombre", usuario.getNombre()));
    }
}
