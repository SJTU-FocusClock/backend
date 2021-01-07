package com.se.focusclock.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    @NotNull
    @Column(name = "user_id1")
    private int me;

    @NotNull
    @Column(name = "user_id2")
    private int other;
}
