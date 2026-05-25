package com.pruebatecnica.seguros.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.pruebatecnica.seguros.dto.RiesgoRequest;
import com.pruebatecnica.seguros.model.Poliza;
import com.pruebatecnica.seguros.model.Riesgo;
import com.pruebatecnica.seguros.model.enums.EstadoPoliza;
import com.pruebatecnica.seguros.model.enums.TipoPoliza;
import com.pruebatecnica.seguros.service.PolizaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
@Tag(name = "Pólizas", description = "Endpoints para la gestión administrativa de pólizas") 
public class PolizaController {

    private final PolizaService polizaService;

    @Operation(summary = "Lista todas las pólizas", description = "Retorna un listado de pólizas filtradas por tipo y estado. Si no se proveen filtros, retorna todas.")
    @GetMapping
    public List<Poliza> listar(
            @Parameter(description = "Tipo de póliza (INDIVIDUAL o COLECTIVA)") @RequestParam(required = false) TipoPoliza tipo, 
            @Parameter(description = "Estado actual de la póliza") @RequestParam(required = false) EstadoPoliza estado) {
        return polizaService.listarPolizas(tipo, estado);
    }

    @Operation(summary = "Lista riesgos de una póliza", description = "Obtiene la lista de riesgos asociados a un ID de póliza específico.")
    @GetMapping("/{id}/riesgos")
    public List<Riesgo> listarRiesgos(@Parameter(description = "ID único de la póliza") @PathVariable Long id) {
        return polizaService.listarPolizas(null, null).stream()
                .filter(p -> p.getId().equals(id))
                .findFirst().map(Poliza::getRiesgos).orElse(Collections.emptyList());
    }

    @Operation(summary = "Renueva una póliza", description = "Aplica el incremento de IPC al canon y prima, y cambia el estado de la póliza a RENOVADA.")
    @PostMapping("/{id}/renovar")
    public Poliza renovar(@Parameter(description = "ID de la póliza a renovar") @PathVariable Long id) {
        return polizaService.renovarPoliza(id);
    }

    @Operation(summary = "Cancela una póliza", description = "Cambia el estado a CANCELADA y cancela en cascada todos los riesgos asociados.")
    @PostMapping("/{id}/cancelar")
    public Poliza cancelar(@Parameter(description = "ID de la póliza a cancelar") @PathVariable Long id) {
        return polizaService.cancelarPoliza(id);
    }

    @Operation(summary = "Agrega un riesgo a la póliza", description = "Crea un nuevo riesgo asociado a la póliza. Valida que las pólizas INDIVIDUALES no superen un riesgo.")
    @PostMapping("/{id}/riesgos")
    public Riesgo agregarRiesgo(
            @Parameter(description = "ID de la póliza padre") @PathVariable Long id, 
            @Valid @RequestBody RiesgoRequest riesgoRequest) {
        return polizaService.agregarRiesgo(id, riesgoRequest);
    }
}