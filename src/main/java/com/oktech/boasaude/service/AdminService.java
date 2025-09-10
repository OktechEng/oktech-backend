package com.oktech.boasaude.service;

import com.oktech.boasaude.dto.AdminUserViewDto;
import com.oktech.boasaude.entity.UserStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminUserViewDto> getAllUsers(Pageable pageable);
    AdminUserViewDto getUserDetails(UUID userId);
    AdminUserViewDto updateUserStatus(UUID userId, UserStatus newStatus);
    void deleteUser(UUID userId); // Manter a exclusão, que agora fará o soft delete para DELETED
}