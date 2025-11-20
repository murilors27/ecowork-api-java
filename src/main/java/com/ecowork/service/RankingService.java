package com.ecowork.service;

import com.ecowork.dto.ranking.RankingUsuarioDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.models.Usuario;
import com.ecowork.repository.UsuarioRepository;
import com.ecowork.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final AuthUtils authUtils;

    @Cacheable(value = "rankingGlobal")
    public List<RankingUsuarioDTO> rankingGlobal() {

        List<Usuario> ordenados = usuarioRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Usuario::getPontosTotais).reversed())
                .toList();

        AtomicInteger posicao = new AtomicInteger(1);

        return ordenados.stream()
                .map(u -> RankingUsuarioDTO.builder()
                        .usuarioId(u.getId())
                        .nome(u.getNome())
                        .empresa(u.getEmpresa().getNome())
                        .pontosTotais(u.getPontosTotais())
                        .posicao(posicao.getAndIncrement())
                        .build())
                .toList();
    }

    @Cacheable(value = "rankingEmpresa", key = "#empresaId")
    public List<RankingUsuarioDTO> rankingPorEmpresa(Long empresaId) {

        List<Usuario> ordenados = usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getEmpresa().getId().equals(empresaId))
                .sorted(Comparator.comparingInt(Usuario::getPontosTotais).reversed())
                .toList();

        AtomicInteger posicao = new AtomicInteger(1);

        return ordenados.stream()
                .map(u -> RankingUsuarioDTO.builder()
                        .usuarioId(u.getId())
                        .nome(u.getNome())
                        .empresa(u.getEmpresa().getNome())
                        .pontosTotais(u.getPontosTotais())
                        .posicao(posicao.getAndIncrement())
                        .build())
                .toList();
    }

    public RankingUsuarioDTO posicaoUsuarioLogado() {
        Usuario u = authUtils.getUsuarioLogado();
        return posicaoUsuario(u.getId());
    }

    public RankingUsuarioDTO posicaoUsuario(Long userId) {

        Usuario usuario = usuarioService.buscarEntity(userId);

        List<Usuario> ordenados = usuarioRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Usuario::getPontosTotais).reversed())
                .toList();

        int posicao = ordenados.indexOf(usuario) + 1;

        if (posicao == 0) throw new NotFoundException("Usuário não encontrado no ranking.");

        return RankingUsuarioDTO.builder()
                .usuarioId(usuario.getId())
                .empresa(usuario.getEmpresa().getNome())
                .nome(usuario.getNome())
                .pontosTotais(usuario.getPontosTotais())
                .posicao(posicao)
                .build();
    }
}