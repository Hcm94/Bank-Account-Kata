package com.test.bankaccountkata.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationType {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal");

    final String operation;
}
