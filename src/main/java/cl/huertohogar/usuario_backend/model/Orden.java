package cl.huertohogar.usuario_backend.model;

import java.time.LocalDate;

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
@Table(name = "orden")
@Schema(description = "Entidad que representa una orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden", nullable = false)
    @Schema(description = "Identificador único de la orden")
    private Integer idOrden;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Usuario que realiza la orden")
    private Usuario usuario;

    @Column(name= "fecha_orden", nullable = false)
    @Schema(description = "Fecha en que se realizó la orden")
    private LocalDate fechaOrden;

    @Column(name= "estado", nullable = false)
    @Schema(description = "Estado de la orden")
    private String estado;

    @Column(name= "total_orden", nullable = false)
    @Schema(description = "Total de la orden")
    private Double totalOrden;

    @Column(name= "direccion_envio", nullable = false)
    @Schema(description = "Dirección de envío de la orden")
    private String direccionEnvio;

}