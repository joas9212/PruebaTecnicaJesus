package com.pruebatecnica.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pruebatecnica.seguros.model.Riesgo;

public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {
	
}