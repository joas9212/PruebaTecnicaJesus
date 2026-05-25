package com.pruebatecnica.seguros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa el contrato de comunicación hacia el sistema CORE legado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de transferencia de datos para notificaciones al sistema CORE")
public class CoreEventoRequest {

    @Schema(description = "Tipo de evento o acción realizada", example = "ACTUALIZACION", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del evento es obligatorio")
    private String evento;

    @Schema(description = "Identificador único de la póliza afectada", example = "555", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la póliza es obligatorio")
    private Long polizaId;
}