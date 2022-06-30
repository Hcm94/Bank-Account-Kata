package com.test.bankaccountkata.util;

import com.test.bankaccountkata.dto.AccountDTO;
import com.test.bankaccountkata.dto.OperationDTO;
import com.test.bankaccountkata.entity.Account;
import com.test.bankaccountkata.entity.Operation;
import com.test.bankaccountkata.enumeration.OperationType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class AccountMapperTest {

    private Account account;

    @Before
    public void setUp() throws Exception {
        account = new Account();
        account.setId(12L);
        account.setBalance(50000);
        for(long i =0;i<10;i++) {
            Operation operation = new Operation(i, Instant.now().minusSeconds(i), (i % 2 == 0) ? OperationType.DEPOSIT : OperationType.WITHDRAWAL,
                    10000L, 40000L,account);

            account.getOperations().add(operation);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void entityToDTO() {
        AccountDTO accountDto = AccountMapper.entityToDTO(account);
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getLatestOperations()).hasSize(5);
        assertThat(accountDto.getLatestOperations().size()).isLessThanOrEqualTo(account.getOperations().size());
        assertThat(accountDto.getLatestOperations()).isSortedAccordingTo(Comparator.comparing(OperationDTO::getDate).reversed());
    }
}