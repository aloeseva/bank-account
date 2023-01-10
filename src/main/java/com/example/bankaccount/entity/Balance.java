package com.example.bankaccount.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "balance")
@Data
@NoArgsConstructor
public class Balance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private Long amount;

    public Balance(Long amount) {
        this.amount = amount;
    }
}
