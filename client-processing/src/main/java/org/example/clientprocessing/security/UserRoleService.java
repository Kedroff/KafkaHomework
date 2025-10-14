package org.example.clientprocessing.security;

import enums.security.RoleName;
import org.example.clientprocessing.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final UserRepository userRepository;

    public UserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isBlockedClient(String login) {
        return userRepository.findByLogin(login)
                .map(u -> u.getRoles() != null && u.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_BLOCKED_CLIENT))
                .orElse(false);
    }
}


