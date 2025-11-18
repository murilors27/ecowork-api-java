package com.ecowork.controller.api;

import com.ecowork.dto.auth.AuthRequestDTO;
import com.ecowork.dto.auth.AuthResponseDTO;
import com.ecowork.dto.auth.AuthRegisterRequestDTO;
import com.ecowork.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody AuthRegisterRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }
}