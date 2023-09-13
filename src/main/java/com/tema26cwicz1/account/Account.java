package com.tema26cwicz1.account;

import com.tema26cwicz1.bet.Bet;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  firstName;
    private String  lastName;
    @Column(unique = true)
    private String  email;
    private BigDecimal Balance;
    @OneToMany(mappedBy = "account")
    private List<Bet> accountBets = new ArrayList<>();

    //TO DO


}
