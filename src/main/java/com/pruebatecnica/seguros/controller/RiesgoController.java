package com.pruebatecnica.seguros.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.seguros.model.Riesgo;
import com.pruebatecnica.seguros.service.PolizaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador dedicado a las operaciones específicas de Riesgos.
 * Permite gestionar el ciclo de vida de los riesgos de manera independiente.
 */
@RestController
@RequestMapping("/riesgos")
@RequiredArgsConstructor
@Tag(name = "Riesgos", description = "Endpoints para la gestión de riesgos asociados a las pólizas")
public class RiesgoController {

    private final PolizaService polizaService;

    @Operation(
        summary = "Cancela un riesgo específico", 
        description = "Cambia el estado de un riesgo a CANCELADO mediante su ID único."
    )
    @PostMapping("/{id}/cancelar")
    public Riesgo cancelarRiesgo(
            @Parameter(description = "ID único del riesgo a cancelar") @PathVariable Long id) {
        return polizaService.cancelarRiesgo(id);
    }
}