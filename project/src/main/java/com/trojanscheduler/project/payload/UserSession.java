package com.trojanscheduler.project.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSession {

    private String username;

    private Long currentCalendarId;

}
