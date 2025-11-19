package com.ecowork.service;

import com.ecowork.dto.sensor.SensorCreateDTO;
import com.ecowork.dto.sensor.SensorResponseDTO;
import com.ecowork.dto.sensor.SensorUpdateDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.SensorMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.Sensor;
import com.ecowork.models.Usuario;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.SensorRepository;
import com.ecowork.security.AuthUtils;
import com.ecowork.security.EmpresaSecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final EmpresaRepository empresaRepository;
    private final AuthUtils authUtils;
    private final EmpresaSecurityValidator empresaSecurityValidator;

    public SensorResponseDTO criar(SensorCreateDTO dto) {

        Usuario logado = authUtils.getUsuarioLogado();

        empresaSecurityValidator.validarAcessoEmpresa(dto.getEmpresaId(), logado);

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        Sensor sensor = SensorMapper.toEntity(dto, empresa);
        sensorRepository.save(sensor);

        return SensorMapper.toDTO(sensor);
    }

    public SensorResponseDTO buscarPorId(Long id) {

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                sensor.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        return SensorMapper.toDTO(sensor);
    }

    public List<SensorResponseDTO> listarTodos() {
        return sensorRepository.findAll()
                .stream()
                .map(SensorMapper::toDTO)
                .toList();
    }

    public List<SensorResponseDTO> listarPorEmpresaId(Long empresaId) {

        empresaSecurityValidator.validarAcessoEmpresa(
                empresaId,
                authUtils.getUsuarioLogado()
        );

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        return sensorRepository.findByEmpresa(empresa)
                .stream()
                .map(SensorMapper::toDTO)
                .toList();
    }

    public SensorResponseDTO atualizar(Long id, SensorUpdateDTO dto) {

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                sensor.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        if (dto.getTipoSensor() != null)
            sensor.setTipoSensor(dto.getTipoSensor());

        if (dto.getLocalizacao() != null)
            sensor.setLocalizacao(dto.getLocalizacao());

        sensorRepository.save(sensor);

        return SensorMapper.toDTO(sensor);
    }

    public void deletar(Long id) {

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                sensor.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        sensorRepository.delete(sensor);
    }
}