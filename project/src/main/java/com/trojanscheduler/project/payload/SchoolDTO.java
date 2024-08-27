package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class SchoolDTO implements Comparable<SchoolDTO>{

    private int id;
    private String name;

    @Override
    public int compareTo(SchoolDTO schoolDTO) {
        return this.name.compareTo(schoolDTO.name);
    }
}
