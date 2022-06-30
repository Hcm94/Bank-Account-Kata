package com.test.bankaccountkata.dao;

import com.test.bankaccountkata.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<Account, Long> {

}
