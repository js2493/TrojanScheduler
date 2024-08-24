package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDTO {

    private String username;
    private String password;
    private String email;

}
