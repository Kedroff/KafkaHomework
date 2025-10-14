package org.example.clientprocessing.service.impl;

import dto.clientProcessing.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.example.clientprocessing.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Создать пользователя: {}", userDto);
        return null;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получить пользователя по id: {}", id);
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получить всех пользователей");
        return Collections.emptyList();
    }
}
