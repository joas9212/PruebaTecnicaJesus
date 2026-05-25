package com.pruebatecnica.seguros.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pruebatecnica.seguros.model.enums.EstadoRiesgo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa un riesgo específico cubierto por una póliza")
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del riesgo", example = "101")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id")
    @JsonBackReference
    @Schema(description = "Póliza a la que pertenece este riesgo", hidden = true) // 'hidden = true' evita bucles infinitos en Swagger
    private Poliza poliza;
    
    @Schema(description = "Descripción detallada del riesgo", example = "Riesgo de daño por inundación")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del riesgo", example = "ACTIVO")
    private EstadoRiesgo estado = EstadoRiesgo.ACTIVO;
}