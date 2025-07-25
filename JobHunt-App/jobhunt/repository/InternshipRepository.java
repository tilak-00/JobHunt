package com.example.jobhunt.repository;

import com.example.jobhunt.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    /**
     * Fetch internships whose last date is before the given date.
     * Useful for filtering expired internships.
     */
    List<Internship> findByLastDateBefore(LocalDate date);

    /**
     * (Optional Example) Fetch internships by company name
     * List<Internship> findByCompanyIgnoreCase(String company);
     * 
     * You can define more custom queries as needed, like filtering by location, mode, etc.
     */
}
