package com.se.focusclock.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clock_record")
public class ClockRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;


    @NotNull
    @Column(name = "user_id")
    private int userid;

    @NotNull
    @Column(name = "start_time")
    private Time start;

    @NotNull
    @Column(name = "end_time")
    private Time end;


    @NotNull
    @Column(name = "date")
    private Date date;

}
