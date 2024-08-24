package com.trojanscheduler.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private TrojanUser user;

    private String name;

    @ManyToMany
    @JoinTable(
            name="calendar_section",
            joinColumns = @JoinColumn(name="calendar_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> sections = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
