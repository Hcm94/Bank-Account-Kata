package com.test.bankaccountkata.controller;

import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.dto.OperationCommand;
import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.exception.NoSuchAccountException;
import com.test.bankaccountkata.service.AccountService;
import com.test.bankaccountkata.service.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final OperationService operationService;

    @GetMapping("{accountId}")
    public AccountDTO printAccountStatement(@PathVariable long accountId) throws NoSuchAccountException {
        return accountService.printStatement(accountId);
    }

    @GetMapping("{accountId}/history")
    public List<OperationDTO> showOperationsList(@PathVariable long accountId) throws NoSuchAccountException {
        return accountService.getAllOperations(accountId);
    }

    @PutMapping(value = "deposit")
    public AccountDTO deposit(@RequestBody OperationCommand operationCommand) throws NoSuchAccountException {
        return operationService.deposit(operationCommand.getAccountId(),operationCommand.getAmount());
    }

    @PutMapping(value = "withdraw")
    public AccountDTO withdraw(@RequestBody OperationCommand operationCommand) throws NoSuchAccountException {
        return operationService.withdraw(operationCommand.getAccountId(),operationCommand.getAmount());
    }
}
