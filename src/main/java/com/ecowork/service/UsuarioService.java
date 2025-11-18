package com.ecowork.service;

import com.ecowork.dto.usuario.UsuarioCreateDTO;
import com.ecowork.dto.usuario.UsuarioResponseDTO;
import com.ecowork.exception.BusinessException;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.UsuarioMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.Usuario;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe um usuário com este e-mail.");
        }

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Usuario usuario = UsuarioMapper.toEntity(dto, empresa, senhaCriptografada);
        usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    public Usuario buscarEntity(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }
}