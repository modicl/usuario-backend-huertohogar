package cl.huertohogar.usuario_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.huertohogar.usuario_backend.model.Ciudad;
import cl.huertohogar.usuario_backend.service.CiudadService;
import cl.huertohogar.usuario_backend.config.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/ciudades")
@Tag(name = "API Ciudad", description = "Operaciones relacionadas con las ciudades")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;

    @Operation(
        summary = "Crear una nueva ciudad",
        description = "Crea una nueva ciudad en el sistema con su nombre y región asociada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Ciudad creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Ciudad.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos ingresados NO válidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"El nombre de la ciudad es obligatorio\",\"status\":400}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @PostMapping("")
    public ResponseEntity<Ciudad> createCiudad(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Ingrese datos de la ciudad a crear",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = Ciudad.class),
                    examples = @ExampleObject(
                        name = "Ejemplo de ciudad",
                        value = "{\"idCiudad\":0,\"nombreCiudad\":\"Santiago\",\"region\":{\"idRegion\":1,\"nombreRegion\":\"Metropolitana\"}}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Ciudad ciudad) { 
        Ciudad nuevaCiudad = ciudadService.save(ciudad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCiudad);
    }

    @Operation(
        summary = "Listar todas las ciudades",
        description = "Obtiene la lista completa de ciudades registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de ciudades obtenida exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron ciudades",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"No se encontraron ciudades\",\"status\":404}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("")
    public ResponseEntity<List<Ciudad>> getCiudades() {
        return ResponseEntity.ok(ciudadService.findAll());
    }

    @Operation(
        summary = "Obtener ciudad por ID",
        description = "Busca y retorna una ciudad por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ciudad encontrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ciudad no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"Ciudad no encontrada con id: 999\",\"status\":404}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> getCiudadById(
            @Parameter(description = "ID de la ciudad a buscar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(ciudadService.findById(id));
    }

    @Operation(
        summary = "Actualizar ciudad completa",
        description = "Actualiza todos los campos de una ciudad existente. Requiere enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ciudad actualizada exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Ciudad no encontrada",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @RequireRole({"ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> updateCiudad(
            @Parameter(description = "ID de la ciudad a actualizar", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos completos de la ciudad actualizada",
                content = @Content(
                    schema = @Schema(implementation = Ciudad.class),
                    examples = @ExampleObject(
                        value = "{\"idCiudad\":1,\"nombreCiudad\":\"Santiago Centro\",\"region\":{\"idRegion\":1,\"nombreRegion\":\"Metropolitana\"}}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Ciudad ciudad) {
        Ciudad ciudadActualizada = ciudadService.update(id, ciudad);
        return ResponseEntity.ok(ciudadActualizada);
    }

    @Operation(
        summary = "Actualizar ciudad parcialmente",
        description = "Actualiza solo los campos especificados de una ciudad. No es necesario enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ciudad actualizada parcialmente"),
        @ApiResponse(responseCode = "404", description = "Ciudad no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @RequireRole({"ADMIN"})
    @PatchMapping("/{id}")
    public ResponseEntity<Ciudad> partialUpdateCiudad(
            @Parameter(description = "ID de la ciudad", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Campos a actualizar (solo los que se envíen serán modificados)",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Actualizar nombre",
                        value = "{\"nombreCiudad\":\"Valparaíso\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Ciudad ciudad) {
        Ciudad ciudadActualizada = ciudadService.partialUpdate(id, ciudad);
        return ResponseEntity.ok(ciudadActualizada);
    }

    @Operation(
        summary = "Eliminar ciudad",
        description = "Elimina permanentemente una ciudad del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ciudad eliminada exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Ciudad no encontrada",
            content = @Content(mediaType = "application/json")
        )
    })
    @RequireRole({"ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCiudad(
            @Parameter(description = "ID de la ciudad a eliminar", example = "1")
            @PathVariable Integer id) {
        ciudadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Buscar ciudades por región",
        description = "Obtiene todas las ciudades que pertenecen a una región específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ciudades encontradas"),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron ciudades para esa región",
            content = @Content(mediaType = "application/json")
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("/region/{idRegion}")
    public ResponseEntity<List<Ciudad>> getCiudadesPorRegion(
            @Parameter(description = "ID de la región", example = "1")
            @PathVariable Integer idRegion) {
        return ResponseEntity.ok(ciudadService.findByIdRegion(idRegion));
    }

}
