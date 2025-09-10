package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.AdminUserViewDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserStatus;
import com.oktech.boasaude.repository.UserRepository;
import com.oktech.boasaude.service.AdminService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public Page<AdminUserViewDto> getAllUsers(Pageable pageable) {
        // O @Where na entidade já filtra os usuários 'DELETED'
        return userRepository.findAll(pageable).map(AdminUserViewDto::new);
    }

    @Override
    public AdminUserViewDto getUserDetails(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new AdminUserViewDto(user);
    }

    @Override
    public AdminUserViewDto updateUserStatus(UUID userId, UserStatus newStatus) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Regra de negócio: não permitir que um admin delete a si mesmo via status
        // Adicionar mais regras conforme necessário (ex: não suspender outros admins)
        if (newStatus == UserStatus.DELETED) {
            throw new IllegalArgumentException("Use the DELETE endpoint to delete a user.");
        }

        user.setStatus(newStatus);
        User updatedUser = userRepository.save(user);
        return new AdminUserViewDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        // O @SQLDelete na entidade fará o soft delete para o status 'DELETED'
        userRepository.deleteById(userId);
    }
}