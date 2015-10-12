package com.hearthlogs.server.match;

import javax.persistence.*;

@Entity
@Table(name = "turn")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turn_number")
    private int turnNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id")
    private Match match;





}
