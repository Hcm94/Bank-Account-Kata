package com.test.bankaccountkata.service;

import com.test.bankaccountkata.dao.OperationDAO;
import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;
import com.test.bankaccountkata.enumeration.OperationType;
import com.test.bankaccountkata.exception.NoSuchAccountException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OperationServiceTest {
    @Mock
    private AccountService accountService;

    @Mock
    private OperationDAO operationRepository;

    @InjectMocks
    private OperationService operationService;

    private Account account ;
    private Operation operation;
    @Before
    public void setUp(){
        account = new Account();
        account.setBalance(5000);
        account.setId(12L);
        operation = new Operation(1L, Instant.now(), OperationType.DEPOSIT,10000L, null, null);
    }

    @Test
    public void createAndPerformOperation_should_perform_deposit() throws NoSuchAccountException {
        when(accountService.getAccountEntityById(anyLong())).thenReturn(account);
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation(account,1000,OperationType.DEPOSIT);
        assertThat(operation.getAmount()).isEqualTo(1000);
        assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void createAndPerformOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(accountService.getAccountEntityById(anyLong())).thenReturn(account);
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation(account,5000,OperationType.WITHDRAWAL);
        assertThat(operation.getAmount()).isEqualTo(-5000);
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(accountService.getAccountEntityById(anyLong())).thenReturn(account);
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.deposit(12L,1200);
        assertThat(dto.getLatestOperations().size()).isEqualTo(1);
        assertThat(dto.getLatestOperations().get(0).getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(dto.getLatestOperations().get(0).getAmount()).isEqualTo(1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test
    public void doWithdrawal_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(accountService.getAccountEntityById(anyLong())).thenReturn(account);
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.withdraw(12L,1200);
        assertThat(dto.getLatestOperations().size()).isEqualTo(1);
        assertThat(dto.getLatestOperations().get(0).getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(dto.getLatestOperations().get(0).getAmount()).isEqualTo(-1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }

}