package com.example.security.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Roles extends Persistable {

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole name;

}
