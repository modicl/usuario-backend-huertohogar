package cl.huertohogar.usuario_backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ciudad")
@Schema(description = "Entidad que representa una ciudad")
public class Ciudad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad", nullable = false)
    @Schema(description = "Identificador único de la ciudad")
    private Integer idCiudad;

    @Column(name = "nombre_ciudad", nullable = false)
    @Schema(description = "Nombre de la ciudad")
    private String nombreCiudad;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    @Schema(description = "Región a la que pertenece la ciudad")
    private Region region;

}
