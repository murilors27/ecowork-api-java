package com.ecowork.repository;

import com.ecowork.models.Pontuacao;
import com.ecowork.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontuacaoRepository extends JpaRepository<Pontuacao, Long> {

    List<Pontuacao> findByUsuario(Usuario usuario);
}