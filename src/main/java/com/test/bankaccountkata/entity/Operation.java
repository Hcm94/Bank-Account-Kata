package com.test.bankaccountkata.entity;

import com.test.bankaccountkata.enumeration.OperationType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "operation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequenceGenerator")
    private Long id;

    private Instant date;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private Long amount ;

    @Column(name = "account_balance_after_operation")
    private Long accountBalanceAfterOperation;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
