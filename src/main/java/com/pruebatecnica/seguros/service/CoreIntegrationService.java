package com.pruebatecnica.seguros.service;

import com.pruebatecnica.seguros.dto.CoreEventoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio de integración encargado de la comunicación con sistemas externos (Legacy Core).
 * Gestiona el envío de eventos de notificación para mantener la sincronización de estados.
 */
@Service
@Slf4j
public class CoreIntegrationService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CORE_MOCK_URL = "http://localhost:8080/core-mock/evento";

    /**
     * Notifica un evento de cambio de estado al sistema Core de forma síncrona.
     * En un entorno productivo real, esta comunicación debería delegarse a un Message Broker (ej. Kafka/RabbitMQ)
     * o protegerse mediante un patrón Circuit Breaker (Resilience4j).
     * * @param polizaId El identificador único de la póliza afectada.
     * @param accion   El tipo de acción o evento ocurrido (ej. RENOVACION, NUEVO_RIESGO).
     */
    public void notificarCore(Long polizaId, String accion) {
        try {
            // Utilizamos el DTO tipado para asegurar el contrato JSON
            CoreEventoRequest payload = new CoreEventoRequest(accion, polizaId);
            
            log.info("Enviando notificación al CORE para póliza ID: {} con acción: {}", polizaId, accion);
            
            restTemplate.postForEntity(CORE_MOCK_URL, payload, String.class);
            
            log.info("Notificación enviada exitosamente al CORE.");
            
        } catch (Exception e) {
            // Registramos el error sin detener el flujo principal de negocio (Fail-safe log)
            log.error("Fallo al notificar al CORE. (Circuit Breaker se abriría aquí para evitar cascada de errores)", e);
        }
    }
}