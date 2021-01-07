package com.se.focusclock.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clock")
public class Clock {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    @NotNull
    @Column(name = "user_id")
    private int owner;

    @NotNull
    @Column(name = "set_user_id")
    private int setter;

    @NotNull
    @Column(name = "status")
    private boolean status;

    @NotNull
    @Column(name = "ring")
    private int ring;


    @NotNull
    @Column(name = "tag")
    private String tag;

    @Column(name = "game_id")
    private int gameid;

    @NotNull
    @Column(name = "game_type")
    private boolean gametype;

    @NotNull
    @Column(name = "time")
    private Time time;

    @NotNull
    @Column(name = "week")
    private byte week;

}
