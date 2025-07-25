package com.example.jobhunt.repository;

import com.example.jobhunt.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Custom query to find jobs where lastDate is before the current date
    List<Job> findByLastDateBefore(LocalDate date);
}
