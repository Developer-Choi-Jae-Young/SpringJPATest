package com.example.demo.jpateam2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team2 {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
