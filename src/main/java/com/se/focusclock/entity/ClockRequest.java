package com.se.focusclock.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clock_request")
public class ClockRequest {



    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="send_user_id",insertable=false , updatable=false)//设置在article表中的关联字段(外键)
    private User sendUser;

    @NotNull
    @Column(name = "send_user_id")
    private int sender;



    @NotNull
    @Column(name = "receive_user_id")
    private int receiver;

    @NotNull
    @Column(name = "type")
    private int type;

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

    @NotNull
    @Column(name = "request_time")
    private Timestamp requesttime;
}
