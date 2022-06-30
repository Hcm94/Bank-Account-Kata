package com.test.bankaccountkata.util;

import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDTO entityToDTO(Account accountEntity) {
        AccountDTO dto = new AccountDTO();
        dto.setBalance(accountEntity.getBalance());
        List<OperationDTO> lastOps = accountEntity.getOperations().stream().sorted(Comparator.comparing(Operation::getDate).reversed())
                .limit(5)
                .map(OperationMapper::entityToDTO)
                .collect(Collectors.toList());
        dto.setLatestOperations(lastOps);
        return dto;
    }
}
