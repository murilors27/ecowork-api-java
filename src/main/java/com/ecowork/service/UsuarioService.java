package com.ecowork.service;

import com.ecowork.dto.usuario.*;
import com.ecowork.exception.BusinessException;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.UsuarioMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.Usuario;
import com.ecowork.models.enums.RoleUsuario;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.UsuarioRepository;
import com.ecowork.security.AuthUtils;
import com.ecowork.security.EmpresaSecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmpresaSecurityValidator empresaSecurityValidator;
    private final AuthUtils authUtils;

    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail()))
            throw new BusinessException("Já existe um usuário com este e-mail.");

        Usuario logado = authUtils.getUsuarioLogado();

        empresaSecurityValidator.validarAcessoEmpresa(dto.getEmpresaId(), logado);

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        Usuario usuario = UsuarioMapper.toEntity(dto, empresa, passwordEncoder.encode(dto.getSenha()));
        usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorIdComRegra(Long id, String emailLogado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Usuario logado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                usuario.getEmpresa().getId(),
                logado
        );

        if (usuario.getEmail().equals(emailLogado))
            return UsuarioMapper.toDTO(usuario);

        if (logado.getRole() == RoleUsuario.SYSTEM_ADMIN ||
                logado.getRole() == RoleUsuario.COMPANY_ADMIN)
            return UsuarioMapper.toDTO(usuario);

        throw new BusinessException("Você não tem permissão para visualizar este usuário.");
    }

    public List<UsuarioResponseDTO> listarTodos() {

        Usuario logado = authUtils.getUsuarioLogado();

        if (logado.getRole() != RoleUsuario.SYSTEM_ADMIN)
            throw new BusinessException("Apenas system admin pode listar todos os usuários.");

        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dto, String emailLogado) {

        Usuario usuario = buscarEntity(id);

        Usuario logado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                usuario.getEmpresa().getId(),
                logado
        );

        boolean ehOProprio = usuario.getEmail().equals(emailLogado);
        boolean ehAdmin = logado.getRole().name().contains("ADMIN");

        if (!ehOProprio && !ehAdmin)
            throw new BusinessException("Você não pode atualizar este usuário.");

        usuario.setNome(dto.getNome());
        usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarEntity(id);

        empresaSecurityValidator.validarAcessoEmpresa(
                usuario.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        usuarioRepository.delete(usuario);
    }

    public Usuario buscarEntity(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public UsuarioResponseDTO buscarPorEmailDTO(String email) {
        return UsuarioMapper.toDTO(
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."))
        );
    }

    public List<UsuarioEmpresaListDTO> listarPorEmpresa(Long empresaId) {

        empresaSecurityValidator.validarAcessoEmpresa(
                empresaId,
                authUtils.getUsuarioLogado()
        );

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        return empresa.getUsuarios().stream()
                .map(u -> UsuarioEmpresaListDTO.builder()
                        .id(u.getId())
                        .nome(u.getNome())
                        .email(u.getEmail())
                        .build()
                ).toList();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }
}