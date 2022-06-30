package com.test.bankaccountkata.service;

import com.test.bankaccountkata.dao.AccountDAO;
import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;
import com.test.bankaccountkata.enumeration.OperationType;
import com.test.bankaccountkata.exception.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountDAO bankAccountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @Before
    public void setUp() {
        account = new Account();
        account.setBalance(5000);
        account.setId(12L);
        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation(1L, Instant.now(), OperationType.DEPOSIT, 10000L, 6000L, account));
        account.setOperations(operations);
    }

    @Test(expected = NoSuchAccountException.class)
    public void listAllOperations_should_throw_exception_for_no_such_account() throws Exception {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        accountService.getAllOperations(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void listAllOperations_should_successfully_return_all_account_operations() throws NoSuchAccountException {
        when(bankAccountRepository.findById(12L)).thenReturn(Optional.of(account));
        List<OperationDTO> operations = accountService.getAllOperations(12L);
        assertThat(operations).isNotEmpty();
        assertThat(operations).hasSize(1);
    }

    @Test(expected = NoSuchAccountException.class)
    public void printStatement_should_throw_exception_for_no_such_account() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        accountService.printStatement(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void printStatement_should_successfully_return_current_account_balance() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        AccountDTO accountDto = accountService.printStatement(12L);
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getLatestOperations()).isNotEmpty();
        assertThat(accountDto.getLatestOperations()).hasSameSizeAs(account.getOperations());

        Operation operation = new Operation(5L, Instant.now().minusSeconds(10000), OperationType.DEPOSIT, 10000L,
                account.getBalance() + 10000L, account);
        account.getOperations().add(operation);
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        accountDto = accountService.printStatement(12L);
        assertThat(accountDto.getLatestOperations()).hasSize(2);
        assertThat(accountDto.getLatestOperations()).isSortedAccordingTo(Comparator.comparing(OperationDTO::getDate).reversed());

    }
}