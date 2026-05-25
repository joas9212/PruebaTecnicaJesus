package com.pruebatecnica.seguros.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.seguros.dto.CoreEventoRequest;

@RestController
@RequestMapping("/core-mock")
@Slf4j
@Tag(name = "Core Mock", description = "Endpoints de integración simulada para el sistema CORE legado")
public class CoreMockController {

    @Operation(
        summary = "Recibe eventos de actualización", 
        description = "Endpoint mock que simula la notificación de cambios de estado hacia el sistema CORE legado."
    )
    @PostMapping("/evento")
    public ResponseEntity<Void> recibirEvento(@Valid @RequestBody CoreEventoRequest request) {
        // Ahora usamos el objeto tipado en lugar del Map genérico
        log.info(">>>> [CORE LEGACY MOCK] Petición recibida. Evento: '{}', ID Poliza: {}", 
                request.getEvento(), request.getPolizaId());
        
        return ResponseEntity.ok().build();
    }
}