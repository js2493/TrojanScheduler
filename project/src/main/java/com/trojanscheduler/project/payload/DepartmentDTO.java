package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class DepartmentDTO implements Comparable<DepartmentDTO> {

    private String code;
    private String name;
    private int schoolId;

    @Override
    public int compareTo(DepartmentDTO departmentDTO) {
        return this.name.compareTo(departmentDTO.name);
    }
}
