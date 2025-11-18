package com.ecowork.repository;

import com.ecowork.models.Sensor;
import com.ecowork.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByEmpresa(Empresa empresa);
}