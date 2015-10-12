package com.hearthlogs.server.match;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




}
