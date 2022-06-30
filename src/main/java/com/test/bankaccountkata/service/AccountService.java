package com.test.bankaccountkata.service;

import com.test.bankaccountkata.dao.AccountDAO;
import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.exception.NoSuchAccountException;
import com.test.bankaccountkata.util.AccountMapper;
import com.test.bankaccountkata.util.OperationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountDAO accountDAO;

    @Transactional
    public Account getAccountEntityById (long accountId) throws NoSuchAccountException {
        Optional<Account> accountOptional = accountDAO.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new NoSuchAccountException(": " + accountId);
        }
        return accountOptional.get();
    }

    @Transactional
    public List<OperationDTO> getAllOperations(long accountId) throws NoSuchAccountException {
        return getAccountEntityById(accountId).getOperations().stream().map(OperationMapper::entityToDTO).collect(Collectors.toList());
    }

    @Transactional
    public AccountDTO printStatement(long accountId) throws NoSuchAccountException {
        return AccountMapper.entityToDTO(getAccountEntityById(accountId));
    }
}
