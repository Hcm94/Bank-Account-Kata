package com.test.bankaccountkata.dao;

import com.test.bankaccountkata.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationDAO extends JpaRepository<Operation, Long> {
}
