package com.ecowork.service;

import com.ecowork.models.Pontuacao;
import com.ecowork.models.Usuario;
import com.ecowork.models.enums.TipoPonto;
import com.ecowork.repository.PontuacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PontuacaoService {

    private final PontuacaoRepository pontuacaoRepository;

    @CacheEvict(value = {"rankingGlobal", "rankingEmpresa"}, allEntries = true)
    public void registrarPontos(Usuario usuario, int pontos) {

        Pontuacao p = Pontuacao.builder()
                .usuario(usuario)
                .quantidade(pontos)
                .data(LocalDateTime.now())
                .tipo(TipoPonto.AUTOMATICO)
                .build();

        pontuacaoRepository.save(p);

        usuario.setPontosTotais(
                (usuario.getPontosTotais() == null ? 0 : usuario.getPontosTotais()) + pontos
        );
    }
}