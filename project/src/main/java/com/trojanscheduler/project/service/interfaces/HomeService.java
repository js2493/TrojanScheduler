package com.trojanscheduler.project.service.interfaces;

import com.trojanscheduler.project.model.Calendar;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface HomeService {

    List<Calendar> home(Authentication authentication);
}
