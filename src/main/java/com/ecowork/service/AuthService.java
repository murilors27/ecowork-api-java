package com.ecowork.service;

import com.ecowork.dto.auth.AuthRequestDTO;
import com.ecowork.dto.auth.AuthResponseDTO;
import com.ecowork.exception.BusinessException;
import com.ecowork.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    //private final JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO dto) {
        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail());

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessException("Credenciais inválidas.");
        }

        //String token = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                //.token(token)
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .role(usuario.getRole())
                .build();
    }
}