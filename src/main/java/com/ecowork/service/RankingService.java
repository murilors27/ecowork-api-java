package com.ecowork.service;

import com.ecowork.dto.ranking.RankingUsuarioDTO;
import com.ecowork.models.Usuario;
import com.ecowork.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UsuarioRepository usuarioRepository;

    public List<RankingUsuarioDTO> rankingGlobal() {

        return usuarioRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Usuario::getPontosTotais).reversed())
                .map(u -> RankingUsuarioDTO.builder()
                        .usuarioId(u.getId())
                        .nome(u.getNome())
                        .empresa(u.getEmpresa().getNome())
                        .pontosTotais(u.getPontosTotais())
                        .build())
                .toList();
    }
}