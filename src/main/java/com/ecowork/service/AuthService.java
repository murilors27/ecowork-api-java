package com.ecowork.service;

import com.ecowork.dto.auth.AuthRegisterRequestDTO;
import com.ecowork.dto.auth.AuthRequestDTO;
import com.ecowork.dto.auth.AuthResponseDTO;
import com.ecowork.exception.BusinessException;
import com.ecowork.models.Usuario;
import com.ecowork.models.enums.RoleUsuario;
import com.ecowork.repository.UsuarioRepository;
import com.ecowork.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(AuthRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas."));

        String token = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .role(usuario.getRole())
                .build();
    }

    public AuthResponseDTO register(AuthRegisterRequestDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(RoleUsuario.valueOf(dto.getRole()));

        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .role(usuario.getRole())
                .build();
    }
}