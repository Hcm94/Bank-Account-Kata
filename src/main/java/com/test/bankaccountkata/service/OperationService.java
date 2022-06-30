package com.test.bankaccountkata.service;

import com.google.common.annotations.VisibleForTesting;
import com.test.bankaccountkata.dao.OperationDAO;
import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;
import com.test.bankaccountkata.enumeration.OperationType;
import com.test.bankaccountkata.exception.NoSuchAccountException;
import com.test.bankaccountkata.util.AccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@AllArgsConstructor
public class OperationService {

    private final AccountService accountService;

    private final OperationDAO operationDAO;

    @Transactional
    public AccountDTO withdraw(long accountId, long amount) throws NoSuchAccountException {
        Account account = accountService.getAccountEntityById(accountId);
        Operation operation = createAndPerformOperation(account,amount,OperationType.WITHDRAWAL);
        account.getOperations().add(operation);
        return AccountMapper.entityToDTO(account);
    }

    @Transactional
    public AccountDTO deposit(long accountId, long amount) throws NoSuchAccountException {
        Account account = accountService.getAccountEntityById(accountId);
        Operation operation = createAndPerformOperation(account,amount,OperationType.DEPOSIT);
        account.getOperations().add(operation);
        return AccountMapper.entityToDTO(account);
    }

    @VisibleForTesting
    Operation createAndPerformOperation(Account account, long amount, OperationType operationType) {
        long signedAmount = operationType.equals(OperationType.WITHDRAWAL) ? -1 * amount : amount;
        Operation operation = new Operation();
        operation.setAmount(signedAmount);
        operation.setDate(Instant.now());
        operation.setAccount(account);
        operation.setType(operationType);
        long balanceAfterOp = account.getBalance() + signedAmount;
        account.setBalance(balanceAfterOp);
        operation.setAccountBalanceAfterOperation(balanceAfterOp);
        operationDAO.save(operation);
        return operation;
    }

}
