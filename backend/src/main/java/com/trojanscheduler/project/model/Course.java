package com.trojanscheduler.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name="courses")
@Data
public class Course {
    @Id
    private int id;
    private String department;
    private String courseNumber;
    private String courseName;
    private String units;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String term;

    @OneToMany(mappedBy = "course")
    private List<Section> sectionList = new ArrayList<>();

    public int hashCode() {
        return Objects.hash(id, department, courseNumber);
    }


}
