package com.test.bankaccountkata.util;

import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.entity.Operation;

public class OperationMapper {

    public static OperationDTO entityToDTO(Operation operationEntity) {
        return new OperationDTO(operationEntity.getDate(), operationEntity.getType(), operationEntity.getAmount(),
                operationEntity.getAccount().getId(), operationEntity.getAccountBalanceAfterOperation());
    }
}
