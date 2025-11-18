package com.ecowork.service;

import com.ecowork.dto.registro.RegistroConsumoCreateDTO;
import com.ecowork.dto.registro.RegistroConsumoResponseDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.RegistroConsumoMapper;
import com.ecowork.models.*;
import com.ecowork.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistroConsumoService {

    private final RegistroConsumoRepository registroRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetaSustentavelRepository metaRepository;
    private final SensorRepository sensorRepository;
    private final PontuacaoService pontuacaoService;

    public RegistroConsumoResponseDTO criar(RegistroConsumoCreateDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        MetaSustentavel meta = null;
        if (dto.getMetaId() != null) {
            meta = metaRepository.findById(dto.getMetaId())
                    .orElseThrow(() -> new NotFoundException("Meta não encontrada."));
        }

        Sensor sensor = null;
        if (dto.getSensorId() != null) {
            sensor = sensorRepository.findById(dto.getSensorId())
                    .orElseThrow(() -> new NotFoundException("Sensor não encontrado."));
        }

        RegistroConsumo registro = RegistroConsumoMapper.toEntity(dto, usuario, meta, sensor);
        registroRepository.save(registro);

        int pontos = calcularPontos(registro);
        pontuacaoService.registrarPontos(usuario, pontos);

        usuarioRepository.save(usuario);

        return RegistroConsumoMapper.toDTO(registro);
    }

    private int calcularPontos(RegistroConsumo r) {
        return switch (r.getTipo()) {
            case ENERGIA -> r.getValor().multiply(java.math.BigDecimal.valueOf(10)).intValue();
            case PAPEL -> r.getValor().multiply(java.math.BigDecimal.valueOf(3)).intValue();
            case TRANSPORTE -> r.getValor().intValue();
        };
    }
}