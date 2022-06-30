package com.test.bankaccountkata.dto;

import com.test.bankaccountkata.enumeration.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {

    private Instant date;

    private OperationType type;

    private long amount ;

    private Long accountId;

    private Long accountBalanceAfterOperation;

}
