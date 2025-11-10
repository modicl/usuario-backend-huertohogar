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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.huertohogar.usuario_backend.model.Region;
import cl.huertohogar.usuario_backend.service.RegionService;
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
@RequestMapping("/api/v1/regiones")
@Tag(name = "API Region", description = "Operaciones relacionadas con las regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Operation(
        summary = "Crear una nueva región",
        description = "Crea una nueva región en el sistema con un nombre único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Región creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Region.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos ingresados NO válidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"El nombre de la región es obligatorio\",\"status\":400}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @PostMapping("")
    public ResponseEntity<Region> createRegion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Ingrese datos de la región a crear",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = Region.class),
                    examples = @ExampleObject(
                        name = "Ejemplo de región",
                        value = "{\"idRegion\":0,\"nombreRegion\":\"Metropolitana\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Region region) { 
        Region nuevaRegion = regionService.save(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRegion);
    }

    @Operation(
        summary = "Listar todas las regiones",
        description = "Obtiene la lista completa de regiones registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de regiones obtenida exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron regiones",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"No se encontraron regiones\",\"status\":404}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("")
    public ResponseEntity<List<Region>> getRegiones() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @Operation(
        summary = "Obtener región por ID",
        description = "Busca y retorna una región por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Región encontrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Región no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"timestamp\":\"2025-11-08T10:30:00\",\"message\":\"Región no encontrada con id: 999\",\"status\":404}")
            )
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(
            @Parameter(description = "ID de la región a buscar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @Operation(
        summary = "Actualizar región completa",
        description = "Actualiza todos los campos de una región existente. Requiere enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Región no encontrada",
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
    public ResponseEntity<Region> updateRegion(
            @Parameter(description = "ID de la región a actualizar", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos completos de la región actualizada",
                content = @Content(
                    schema = @Schema(implementation = Region.class),
                    examples = @ExampleObject(
                        value = "{\"idRegion\":1,\"nombreRegion\":\"Región Metropolitana\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Region region) {
        Region regionActualizada = regionService.update(id, region);
        return ResponseEntity.ok(regionActualizada);
    }

    @Operation(
        summary = "Actualizar región parcialmente",
        description = "Actualiza solo los campos especificados de una región. No es necesario enviar todos los datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región actualizada parcialmente"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @RequireRole({"ADMIN"})
    @PatchMapping("/{id}")
    public ResponseEntity<Region> partialUpdateRegion(
            @Parameter(description = "ID de la región", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Campos a actualizar (solo los que se envíen serán modificados)",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Actualizar nombre",
                        value = "{\"nombreRegion\":\"Valparaíso\"}"
                    )
                )
            )
            @org.springframework.web.bind.annotation.RequestBody Region region) {
        Region regionActualizada = regionService.partialUpdate(id, region);
        return ResponseEntity.ok(regionActualizada);
    }

    @Operation(
        summary = "Eliminar región",
        description = "Elimina permanentemente una región del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Región eliminada exitosamente"),
        @ApiResponse(
            responseCode = "404",
            description = "Región no encontrada",
            content = @Content(mediaType = "application/json")
        )
    })
    @RequireRole({"ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(
            @Parameter(description = "ID de la región a eliminar", example = "1")
            @PathVariable Integer id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Buscar región por nombre",
        description = "Obtiene una región específica buscando por su nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región encontrada"),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró región con ese nombre",
            content = @Content(mediaType = "application/json")
        )
    })
    @RequireRole({"ADMIN"})
    @GetMapping("/nombreRegion")
    public ResponseEntity<Region> getRegionPorNombre(
            @Parameter(description = "Nombre de la región", example = "Metropolitana")
            @RequestParam String nombreRegion) {
        return ResponseEntity.ok(regionService.findByNombreRegion(nombreRegion));
    }

    @Operation(
        summary = "Verificar existencia de región",
        description = "Verifica si existe una región con el nombre especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/regionExistente")
    public ResponseEntity<Boolean> existeRegion(
            @Parameter(description = "Nombre de la región", example = "Metropolitana")
            @RequestParam String nombreRegion) {
        return ResponseEntity.ok(regionService.existsByNombreRegion(nombreRegion));
    }

}
