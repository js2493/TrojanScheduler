package com.trojanscheduler.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@Entity(name = "users")
@NoArgsConstructor
public class TrojanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "user_section",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> sectionList = new HashSet<>();

    private String username;
    private String password;

    public TrojanUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int hashCode() {
        return Objects.hash(username, id);
    }


}
