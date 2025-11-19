package com.ecowork.service;

import com.ecowork.dto.registro.RegistroConsumoCreateDTO;
import com.ecowork.dto.registro.RegistroConsumoResponseDTO;
import com.ecowork.exception.NotFoundException;
import com.ecowork.mapper.RegistroConsumoMapper;
import com.ecowork.models.*;
import com.ecowork.models.enums.RoleUsuario;
import com.ecowork.models.enums.TipoConsumo;
import com.ecowork.repository.*;
import com.ecowork.security.AuthUtils;
import com.ecowork.security.EmpresaSecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistroConsumoService {

    private final RegistroConsumoRepository registroRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetaSustentavelRepository metaRepository;
    private final SensorRepository sensorRepository;
    private final PontuacaoService pontuacaoService;
    private final AuthUtils authUtils;
    private final EmpresaSecurityValidator empresaSecurityValidator;

    public RegistroConsumoResponseDTO criar(RegistroConsumoCreateDTO dto) {

        Usuario logado = authUtils.getUsuarioLogado();

        Usuario usuario;
        if (logado.getRole() == RoleUsuario.EMPLOYEE) {
            usuario = logado;
        } else {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        }

        empresaSecurityValidator.validarAcessoEmpresa(
                usuario.getEmpresa().getId(),
                logado
        );

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

    public RegistroConsumoResponseDTO buscarPorId(Long id) {
        RegistroConsumo r = registroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                r.getUsuario().getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        return RegistroConsumoMapper.toDTO(r);
    }

    public Page<RegistroConsumoResponseDTO> listarTodos(int pagina, int tamanho) {
        // apenas system admin pode listar tudo
        Usuario logado = authUtils.getUsuarioLogado();

        if (logado.getRole() != RoleUsuario.SYSTEM_ADMIN)
            throw new com.ecowork.exception.BusinessException("Apenas administradores do sistema podem listar todos os registros.");

        Page<RegistroConsumo> page = registroRepository.findAll(PageRequest.of(pagina, tamanho));
        return page.map(RegistroConsumoMapper::toDTO);
    }

    public Page<RegistroConsumoResponseDTO> listarPorUsuario(Long usuarioId, int pagina, int tamanho) {

        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                u.getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        Page<RegistroConsumo> page = registroRepository.findByUsuario(
                u,
                PageRequest.of(pagina, tamanho)
        );

        return page.map(RegistroConsumoMapper::toDTO);
    }

    public Page<RegistroConsumoResponseDTO> listarPorTipo(TipoConsumo tipo, int pagina, int tamanho) {

        Usuario logado = authUtils.getUsuarioLogado();

        Page<RegistroConsumo> page =
                registroRepository.findByTipo(tipo, PageRequest.of(pagina, tamanho))
                        .map(reg -> {
                            empresaSecurityValidator.validarAcessoEmpresa(
                                    reg.getUsuario().getEmpresa().getId(),
                                    logado
                            );
                            return reg;
                        });

        return page.map(RegistroConsumoMapper::toDTO);
    }

    public void deletar(Long id) {

        RegistroConsumo r = registroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro não encontrado."));

        empresaSecurityValidator.validarAcessoEmpresa(
                r.getUsuario().getEmpresa().getId(),
                authUtils.getUsuarioLogado()
        );

        registroRepository.delete(r);
    }
}