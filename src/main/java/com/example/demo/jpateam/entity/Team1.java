package com.example.demo.jpateam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team1 {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
