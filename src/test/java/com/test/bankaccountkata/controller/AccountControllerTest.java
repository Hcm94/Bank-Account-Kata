package com.test.bankaccountkata.controller;

import com.test.bankaccountkata.BankAccountKataApplication;
import com.test.bankaccountkata.dao.AccountDAO;
import com.test.bankaccountkata.dao.OperationDAO;
import com.test.bankaccountkata.dto.OperationCommand;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;
import com.test.bankaccountkata.enumeration.OperationType;
import com.test.bankaccountkata.service.AccountService;
import com.test.bankaccountkata.service.OperationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.test.bankaccountkata.helper.TestHelper.convertObjectToJsonBytes;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApplication.class)
public class AccountControllerTest {
    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationDAO operationRepository;

    @Autowired
    private AccountDAO bankAccountRepository;

    @Autowired
    private AccountService bankAccountService;

    @Autowired
    private ControllerExceptionHandler globalErrorHandler;

    private MockMvc restMvc;


    @Before
    public void setUp() {

        AccountController bankAccountResources = new AccountController(bankAccountService, operationService);
        this.restMvc = MockMvcBuilders.standaloneSetup(bankAccountResources).setControllerAdvice(globalErrorHandler)
                .build();

    }

    @Test
    public void printAccountState_should_return_error_message_and_404_code_status() throws Exception {
        restMvc.perform(get("/api/account/155555555")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    public void printAccountState_should_return_account_details() throws Exception {
        Account account = new Account();
        account.setBalance(1000);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(get("/api/account/{id}", account.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.latestOperations").isEmpty())
                .andExpect(jsonPath("$.balance").value(account.getBalance()));
    }

    @Test
    public void deposit_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(put("/api/account/deposit")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(new OperationCommand(555555,2522L))))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void deposit_should_perform_a_deposit_operation() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(put("/api/account/deposit")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(new OperationCommand(account.getId(), 15000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(15000));

    }

    @Test
    public void withdrawal_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(put("/api/account/withdrawal")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(new OperationCommand(575556L, 2522))))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void withdrawal_should_perform_a_withdrawal_operation() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(put("/api/account/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(new OperationCommand(account.getId(), 200))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(-200));
    }

    @Test
    @Transactional
    public void showOperationsList_should_list_all_previous_operations() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setType(OperationType.WITHDRAWAL);
        operation.setAmount(2000L);
        operationRepository.saveAndFlush(operation);
        Operation operation2 = new Operation();
        operation2.setAccount(account);
        operation2.setType(OperationType.DEPOSIT);
        operation2.setAmount(2000L);
        operationRepository.saveAndFlush(operation2);
        account.getOperations().add(operation);
        account.getOperations().add(operation2);
        restMvc.perform(get("/api/account/{id}/history", account.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].amount").value(hasItems(operation.getAmount().intValue(),operation2.getAmount().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItems(operation.getType().toString(),operation2.getType().toString())));
    }

    @Test
    public void showOperationsList_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(get("/api/account/{id}/history", 5858585)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}