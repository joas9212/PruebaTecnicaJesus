package com.pruebatecnica.seguros.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pruebatecnica.seguros.model.Poliza;
import com.pruebatecnica.seguros.model.Riesgo;
import com.pruebatecnica.seguros.model.enums.EstadoPoliza;
import com.pruebatecnica.seguros.model.enums.EstadoRiesgo;
import com.pruebatecnica.seguros.model.enums.TipoPoliza;
import com.pruebatecnica.seguros.repository.PolizaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente de inicialización de datos de prueba.
 * Se ejecuta automáticamente al arrancar la aplicación para poblar la base de datos H2 en memoria
 * con escenarios predefinidos, facilitando la validación inmediata de reglas de negocio.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PolizaRepository polizaRepository;

    /**
     * Ejecuta la carga de datos iniciales.
     * Define tres escenarios: 
     * 1. Póliza Individual activa (para validar restricción de riesgos).
     * 2. Póliza Colectiva activa (para validar flujos normales).
     * 3. Póliza Cancelada (para validar restricciones de renovación).
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando la precarga de polizas y riesgos por defecto en H2...");

        // Caso 1: Individual
        Poliza individual = new Poliza();
        individual.setTipo(TipoPoliza.INDIVIDUAL);
        individual.setEstado(EstadoPoliza.ACTIVA);
        individual.setCanonMensual(new BigDecimal("1200000.00"));
        individual.setPrimaTotal(new BigDecimal("14400000.00"));

        Riesgo riesgoInd = new Riesgo();
        riesgoInd.setDescripcion("Riesgo de impago arrendamiento residencial");
        riesgoInd.setEstado(EstadoRiesgo.ACTIVO);
        riesgoInd.setPoliza(individual); 
        
        individual.getRiesgos().add(riesgoInd);
        polizaRepository.save(individual); 

        // Caso 2: Colectiva
        Poliza colectiva = new Poliza();
        colectiva.setTipo(TipoPoliza.COLECTIVA);
        colectiva.setEstado(EstadoPoliza.ACTIVA);
        colectiva.setCanonMensual(new BigDecimal("4500000.00"));
        colectiva.setPrimaTotal(new BigDecimal("54000000.00"));

        Riesgo riesgoCol1 = new Riesgo();
        riesgoCol1.setDescripcion("Riesgo de daños a la propiedad comercial");
        riesgoCol1.setEstado(EstadoRiesgo.ACTIVO);
        riesgoCol1.setPoliza(colectiva);

        Riesgo riesgoCol2 = new Riesgo();
        riesgoCol2.setDescripcion("Riesgo de vacancia extendida");
        riesgoCol2.setEstado(EstadoRiesgo.ACTIVO);
        riesgoCol2.setPoliza(colectiva);

        colectiva.getRiesgos().add(riesgoCol1);
        colectiva.getRiesgos().add(riesgoCol2);
        polizaRepository.save(colectiva);

        // Caso 3: Cancelada
        Poliza cancelada = new Poliza();
        cancelada.setTipo(TipoPoliza.INDIVIDUAL);
        cancelada.setEstado(EstadoPoliza.CANCELADA);
        cancelada.setCanonMensual(new BigDecimal("900000.00"));
        cancelada.setPrimaTotal(new BigDecimal("10800000.00"));

        Riesgo riesgoCanc = new Riesgo();
        riesgoCanc.setDescripcion("Riesgo de incendio (Poliza Inactiva)");
        riesgoCanc.setEstado(EstadoRiesgo.CANCELADO);
        riesgoCanc.setPoliza(cancelada);
        
        cancelada.getRiesgos().add(riesgoCanc);
        polizaRepository.save(cancelada);

        log.info("Precarga finalizada con exito. 3 Polizas y 4 Riesgos creados en total y listos para pruebas.");
    }
}