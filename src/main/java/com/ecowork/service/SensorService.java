package com.ecowork.service;

import com.ecowork.dto.sensor.SensorCreateDTO;
import com.ecowork.dto.sensor.SensorResponseDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.SensorMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.Sensor;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final EmpresaRepository empresaRepository;

    public SensorResponseDTO criar(SensorCreateDTO dto) {

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        Sensor sensor = SensorMapper.toEntity(dto, empresa);
        sensorRepository.save(sensor);

        return SensorMapper.toDTO(sensor);
    }
}