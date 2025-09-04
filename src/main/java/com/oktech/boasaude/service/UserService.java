package com.oktech.boasaude.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;

/**
 * Interface para o serviço de usuário.
 * Define os métodos para operações relacionadas a usuários.
 * 
 * @author Arlindo Neto
 * @version 1.0
 * @author Helder
 * @version 1.1
 */
public interface UserService {
    User createUser(CreateUserDto createUserDto);

    User getUserById(UUID id);

    Page<User> getAllUsers(Pageable pageable);

    User updateUser(UUID id, User user);
 /**
     * Deleta (soft delete) o usuário atualmente autenticado.
     * @param currentUser O objeto do usuário logado, fornecido pelo Spring Security.
     */
     void deleteUserById(UUID userId);

    User getUserByEmail(String email);

    boolean updateUserRole(UUID userId, UserRole userRole);
}
