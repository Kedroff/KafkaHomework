package org.example.accountprocessing.service;

import dto.accountProcessing.AccountDto;
import java.util.List;

public interface AccountService {
    AccountDto createAccount(AccountDto dto);
    AccountDto getAccountById(Long id);
    List<AccountDto> getAllAccounts();
    List<AccountDto> getAccountsByClientId(Long clientId);
    AccountDto updateAccount(Long id, AccountDto dto);
    void deleteAccount(Long id);
}
