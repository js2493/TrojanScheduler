package com.trojanscheduler.project.repository;

import com.trojanscheduler.project.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
