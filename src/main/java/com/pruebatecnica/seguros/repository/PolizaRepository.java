package com.pruebatecnica.seguros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pruebatecnica.seguros.model.Poliza;
import com.pruebatecnica.seguros.model.enums.EstadoPoliza;
import com.pruebatecnica.seguros.model.enums.TipoPoliza;

public interface PolizaRepository extends JpaRepository<Poliza, Long> {
    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);
}