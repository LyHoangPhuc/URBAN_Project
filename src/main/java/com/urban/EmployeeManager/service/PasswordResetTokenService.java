package com.urban.EmployeeManager.service;

import com.urban.EmployeeManager.model.PasswordResetToken;
import com.urban.EmployeeManager.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public PasswordResetToken save(PasswordResetToken data) {

        return passwordResetTokenRepository.save(data);
    }
    public Optional<PasswordResetToken> findByEmail(String name) {
        return passwordResetTokenRepository.findByEmail(name);
    }
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}
