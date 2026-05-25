package com.pruebatecnica.seguros.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pruebatecnica.seguros.model.enums.EstadoPoliza;
import com.pruebatecnica.seguros.model.enums.TipoPoliza;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa una póliza de seguro y sus riesgos asociados")
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único generado por la base de datos", example = "1")
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de póliza contratada", example = "INDIVIDUAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoPoliza tipo;
    
    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del ciclo de vida de la póliza", example = "ACTIVA")
    private EstadoPoliza estado = EstadoPoliza.ACTIVA;
    
    @Schema(description = "Canon mensual definido para la póliza", example = "1200000.00")
    private BigDecimal canonMensual;
    
    @Schema(description = "Prima total de la cobertura", example = "14400000.00")
    private BigDecimal primaTotal;
    
    @OneToMany(mappedBy = "poliza", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(description = "Lista de riesgos cubiertos por esta póliza")
    private List<Riesgo> riesgos = new ArrayList<>();
}