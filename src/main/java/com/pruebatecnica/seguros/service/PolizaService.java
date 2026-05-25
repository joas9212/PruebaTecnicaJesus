package com.pruebatecnica.seguros.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pruebatecnica.seguros.dto.RiesgoRequest;
import com.pruebatecnica.seguros.model.Poliza;
import com.pruebatecnica.seguros.model.Riesgo;
import com.pruebatecnica.seguros.model.enums.EstadoPoliza;
import com.pruebatecnica.seguros.model.enums.EstadoRiesgo;
import com.pruebatecnica.seguros.model.enums.TipoPoliza;
import com.pruebatecnica.seguros.repository.PolizaRepository;
import com.pruebatecnica.seguros.repository.RiesgoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la lógica de negocio central para la gestión de pólizas y sus riesgos asociados.
 * Implementa las reglas de dominio y orquestación con sistemas externos (Core Legacy).
 */
@Service
@RequiredArgsConstructor
public class PolizaService {
	
    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final CoreIntegrationService coreIntegrationService;
    
    private static final BigDecimal IPC_2024 = new BigDecimal("1.09");

    /**
     * Consulta el catálogo de pólizas aplicando filtros opcionales.
     * @param tipo Tipo de póliza (Individual/Colectiva).
     * @param estado Estado actual (Activa/Renovada/Cancelada).
     * @return Lista de pólizas encontradas.
     */
    public List<Poliza> listarPolizas(TipoPoliza tipo, EstadoPoliza estado) {
        if (tipo != null && estado != null) return polizaRepository.findByTipoAndEstado(tipo, estado);
        return polizaRepository.findAll();
    }

    /**
     * Ejecuta el proceso de renovación de una póliza aplicando el ajuste de IPC.
     * @param id ID de la póliza a renovar.
     * @throws ResponseStatusException 404 si no existe, 400 si está cancelada.
     * @return Póliza actualizada.
     */
    @Transactional
    public Poliza renovarPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Póliza no encontrada"));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede renovar una póliza cancelada");
        }

        poliza.setCanonMensual(poliza.getCanonMensual().multiply(IPC_2024));
        poliza.setPrimaTotal(poliza.getPrimaTotal().multiply(IPC_2024));
        poliza.setEstado(EstadoPoliza.RENOVADA);

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreIntegrationService.notificarCore(polizaGuardada.getId(), "RENOVACION");
        return polizaGuardada;
    }

    /**
     * Cancela una póliza y propaga la cancelación a todos sus riesgos asociados (Cascada lógica).
     * @param id ID de la póliza a cancelar.
     * @return Póliza cancelada.
     */
    @Transactional
    public Poliza cancelarPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Póliza no encontrada"));

        poliza.setEstado(EstadoPoliza.CANCELADA);
        poliza.getRiesgos().forEach(r -> r.setEstado(EstadoRiesgo.CANCELADO));

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreIntegrationService.notificarCore(polizaGuardada.getId(), "CANCELACION_POLIZA");
        return polizaGuardada;
    }

    /**
     * Registra un nuevo riesgo en una póliza, validando las restricciones por tipo de póliza.
     * @param polizaId ID de la póliza destino.
     * @param riesgoRequest DTO con los detalles del riesgo.
     * @throws ResponseStatusException 400 si la política de riesgos es violada.
     * @return El nuevo riesgo persistido.
     */
    @Transactional
    public Riesgo agregarRiesgo(Long polizaId, RiesgoRequest riesgoRequest) {
        Poliza poliza = polizaRepository.findById(polizaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Póliza no encontrada"));

        if (poliza.getTipo() == TipoPoliza.INDIVIDUAL && !poliza.getRiesgos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pólizas Individuales solo pueden tener 1 riesgo.");
        }

        Riesgo nuevoRiesgo = new Riesgo();
        nuevoRiesgo.setDescripcion(riesgoRequest.getDescripcion());
        nuevoRiesgo.setEstado(EstadoRiesgo.ACTIVO);
        nuevoRiesgo.setPoliza(poliza);

        Riesgo riesgoGuardado = riesgoRepository.save(nuevoRiesgo);
        coreIntegrationService.notificarCore(poliza.getId(), "NUEVO_RIESGO");
        
        return riesgoGuardado;
    }

    /**
     * Cancela un riesgo de forma individual y notifica al core.
     * @param riesgoId ID del riesgo a cancelar.
     * @return El riesgo actualizado.
     */
    @Transactional
    public Riesgo cancelarRiesgo(Long riesgoId) {
        Riesgo riesgo = riesgoRepository.findById(riesgoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado"));
        
        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        Riesgo riesgoGuardado = riesgoRepository.save(riesgo);
        
        coreIntegrationService.notificarCore(riesgo.getPoliza().getId(), "CANCELACION_RIESGO");
        return riesgoGuardado;
    }
}