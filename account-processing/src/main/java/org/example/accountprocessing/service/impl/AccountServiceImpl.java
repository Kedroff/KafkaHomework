package org.example.accountprocessing.service.impl;

import dto.accountProcessing.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public AccountDto createAccount(AccountDto dto) {
        log.info("Создать счёт: {}", dto);
        return null;
    }

    @Override
    public AccountDto getAccountById(Long id) {
        log.info("Получить счёт по id: {}", id);
        return null;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        log.info("Получить все счета");
        return Collections.emptyList();
    }

    @Override
    public List<AccountDto> getAccountsByClientId(Long clientId) {
        log.info("Получить счета по clientId: {}", clientId);
        return Collections.emptyList();
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto dto) {
        log.info("Обновить счёт id={}, данные={}", id, dto);
        return null;
    }

    @Override
    public void deleteAccount(Long id) {
        log.info("Удалить счёт id={}", id);
    }
}
