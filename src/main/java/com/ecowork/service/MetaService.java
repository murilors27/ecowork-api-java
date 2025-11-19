package com.ecowork.service;

import com.ecowork.dto.meta.MetaCreateDTO;
import com.ecowork.dto.meta.MetaResponseDTO;
import com.ecowork.exception.BusinessException;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.MetaMapper;
import com.ecowork.models.Empresa;
import com.ecowork.models.MetaSustentavel;
import com.ecowork.models.Usuario;
import com.ecowork.models.enums.RoleUsuario;
import com.ecowork.models.enums.StatusMeta;
import com.ecowork.repository.EmpresaRepository;
import com.ecowork.repository.MetaSustentavelRepository;
import com.ecowork.security.AuthUtils;
import com.ecowork.security.EmpresaSecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaSustentavelRepository metaRepository;
    private final EmpresaRepository empresaRepository;
    private final AuthUtils authUtils;
    private final EmpresaSecurityValidator empresaSecurityValidator;

    public MetaResponseDTO criar(MetaCreateDTO dto) {

        Usuario logado = authUtils.getUsuarioLogado();

        empresaSecurityValidator.validarAcessoEmpresa(dto.getEmpresaId(), logado);

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        MetaSustentavel meta = MetaMapper.toEntity(dto, empresa);
        metaRepository.save(meta);

        return MetaMapper.toDTO(meta);
    }

    public MetaResponseDTO buscarPorId(Long id) {
        MetaSustentavel meta = buscarEntity(id);

        empresaSecurityValidator.validarAcessoEmpresa(
                meta.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        return MetaMapper.toDTO(meta);
    }

    public List<MetaResponseDTO> listarTodas() {
        Usuario logado = authUtils.getUsuarioLogado();

        if (logado.getRole() != RoleUsuario.SYSTEM_ADMIN)
            throw new BusinessException("Apenas system admin pode listar todas as metas.");

        return metaRepository.findAll().stream()
                .map(MetaMapper::toDTO)
                .toList();
    }

    public List<MetaResponseDTO> listarPorEmpresa(Long empresaId) {

        empresaSecurityValidator.validarAcessoEmpresa(
                empresaId,
                authUtils.getUsuarioLogado()
        );

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        return metaRepository.findByEmpresa(empresa).stream()
                .map(MetaMapper::toDTO)
                .toList();
    }

    public List<MetaResponseDTO> listarPorStatus(StatusMeta status) {

        Usuario logado = authUtils.getUsuarioLogado();

        if (logado.getRole() != RoleUsuario.SYSTEM_ADMIN &&
                logado.getRole() != RoleUsuario.COMPANY_ADMIN)
            throw new BusinessException("Permissão insuficiente.");

        return metaRepository.findByStatus(status).stream()
                .map(MetaMapper::toDTO)
                .toList();
    }

    public MetaResponseDTO atualizar(Long id, MetaCreateDTO dto) {

        MetaSustentavel meta = buscarEntity(id);

        empresaSecurityValidator.validarAcessoEmpresa(
                meta.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        meta.setTipo(dto.getTipo());
        meta.setValorAlvo(dto.getValorAlvo());
        meta.setDataInicio(dto.getDataInicio());
        meta.setDataFim(dto.getDataFim());
        meta.setEmpresa(empresa);

        metaRepository.save(meta);
        return MetaMapper.toDTO(meta);
    }

    public MetaResponseDTO atualizarStatus(Long id, StatusMeta status) {

        MetaSustentavel meta = buscarEntity(id);

        empresaSecurityValidator.validarAcessoEmpresa(
                meta.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        meta.setStatus(status);

        metaRepository.save(meta);
        return MetaMapper.toDTO(meta);
    }

    public void deletar(Long id) {

        MetaSustentavel meta = buscarEntity(id);

        empresaSecurityValidator.validarAcessoEmpresa(
                meta.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        metaRepository.delete(meta);
    }

    private MetaSustentavel buscarEntity(Long id) {
        return metaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meta não encontrada."));
    }
}