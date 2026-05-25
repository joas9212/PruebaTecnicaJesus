package com.pruebatecnica.seguros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un nuevo riesgo.
 * Utilizado para desacoplar el contrato de API de la entidad JPA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto que representa la información necesaria para registrar un nuevo riesgo")
public class RiesgoRequest {

    @Schema(description = "Descripción detallada del riesgo a cubrir", example = "Riesgo de inundación por lluvias", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción del riesgo no puede estar vacía ni contener solo espacios en blanco")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String descripcion;
    
}