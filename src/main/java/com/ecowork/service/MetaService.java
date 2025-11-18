package com.ecowork.service;

import com.ecowork.dto.meta.MetaCreateDTO;
import com.ecowork.dto.meta.MetaResponseDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.MetaMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.MetaSustentavel;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.MetaSustentavelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaSustentavelRepository metaRepository;
    private final EmpresaRepository empresaRepository;

    public MetaResponseDTO criar(MetaCreateDTO dto) {

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        MetaSustentavel meta = MetaMapper.toEntity(dto, empresa);
        metaRepository.save(meta);

        return MetaMapper.toDTO(meta);
    }
}