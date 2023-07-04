package ru.practicum.ewm.stats.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "apps")
@Setter
@Getter
public class App {
    @Id
    @Column(name = "app_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app_name", unique = true)
    @Size(max = 50, message = "Should be less than 50 symbols")
    private String name;
}
