package com.ecowork.service;

import com.ecowork.dto.pontos.PontuacaoResponseDTO;
import com.ecowork.mapper.PontuacaoMapper;
import com.ecowork.models.Pontuacao;
import com.ecowork.models.Usuario;
import com.ecowork.repository.PontuacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PontuacaoService {

    private final PontuacaoRepository pontuacaoRepository;

    public void registrarPontos(Usuario usuario, int pontos) {

        Pontuacao p = Pontuacao.builder()
                .usuario(usuario)
                .quantidade(pontos)
                .data(LocalDateTime.now())
                .tipo(null)
                .build();

        pontuacaoRepository.save(p);

        usuario.setPontosTotais(usuario.getPontosTotais() + pontos);
    }
}