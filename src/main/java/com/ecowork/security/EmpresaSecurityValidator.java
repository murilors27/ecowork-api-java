package com.ecowork.security;

import com.ecowork.exception.BusinessException;
import com.ecowork.models.Usuario;
import com.ecowork.models.enums.RoleUsuario;
import org.springframework.stereotype.Component;

@Component
public class EmpresaSecurityValidator {

    public void validarAcessoEmpresa(Long empresaId, Usuario usuario) {

        if (usuario.getRole() == RoleUsuario.SYSTEM_ADMIN)
            return;

        if (usuario.getEmpresa() == null)
            throw new BusinessException("Usuário não pertence a nenhuma empresa cadastrada.");

        if (!usuario.getEmpresa().getId().equals(empresaId))
            throw new BusinessException("Você não tem permissão para acessar recursos desta empresa.");
    }
}