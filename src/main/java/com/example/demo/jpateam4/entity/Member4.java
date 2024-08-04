package com.example.demo.jpateam4.entity;

import com.example.demo.jpateam3.entity.Team3;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member4 {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team4 team;

    //추가!!!!
    public void setTeam4(Team4 team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
