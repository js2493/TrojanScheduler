package com.trojanscheduler.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name="sections")
@Data
public class Section {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @ManyToMany(mappedBy="sectionList")
    @JsonIgnore
    private Set<TrojanUser> users = new HashSet<>();

    private String section;
    private String session;
    private String type;
    private int startTime;
    private int endTime;
    private int days;
    private String registered;
    private String instructor;
    private String location;

    public int hashCode() {
        return Objects.hash(id, section);
    }





}
