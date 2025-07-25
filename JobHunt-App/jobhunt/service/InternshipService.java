package com.example.jobhunt.service;

import com.example.jobhunt.dto.InternshipDTO;
import com.example.jobhunt.entity.Internship;
import com.example.jobhunt.repository.InternshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    // ✅ Save internship to DB
    public void saveInternship(InternshipDTO dto) {
        Internship internship = convertDTOToEntity(dto);
        internshipRepository.save(internship);
        System.out.println("✅ Internship saved: " + dto.getRole() + " at " + dto.getCompany());
    }

    // ✅ Get all internships from DB
    public List<InternshipDTO> getAllInternships() {
        List<Internship> internships = internshipRepository.findAll();
        List<InternshipDTO> dtos = new ArrayList<>();

        for (Internship i : internships) {
            InternshipDTO dto = new InternshipDTO();
            dto.setCompany(i.getCompany());
            dto.setRole(i.getRole());
            dto.setEligibility(i.getEligibility());
            dto.setQualification(i.getQualification());
            dto.setSkills(i.getSkills());
            dto.setExperience(i.getExperience());
            dto.setStipend(i.getStipend());
            dto.setLocation(i.getLocation());
            dto.setApplyLink(i.getApplyLink());
            dto.setLastDate(i.getLastDate() != null ? i.getLastDate().toString() : null);

            dtos.add(dto);
        }

        return dtos;
    }

    // ✅ Convert DTO to Entity
    public Internship convertDTOToEntity(InternshipDTO dto) {
        Internship internship = new Internship();
        internship.setCompany(dto.getCompany());
        internship.setRole(dto.getRole());
        internship.setEligibility(dto.getEligibility());
        internship.setQualification(dto.getQualification());
        internship.setSkills(dto.getSkills());
        internship.setExperience(dto.getExperience());
        internship.setStipend(dto.getStipend());
        internship.setLocation(dto.getLocation());
        internship.setApplyLink(dto.getApplyLink());

        try {
            if (dto.getLastDate() != null && !dto.getLastDate().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                internship.setLastDate(LocalDate.parse(dto.getLastDate(), formatter));
            }
        } catch (DateTimeParseException e) {
            internship.setLastDate(null); // fallback in case of parsing error
            System.out.println("⚠️ Invalid date format for internship: " + dto.getLastDate());
        }

        return internship;
    }

    // ✅ Delete internship by ID
    public void deleteInternshipById(Long id) {
        if (internshipRepository.existsById(id)) {
            internshipRepository.deleteById(id);
            System.out.println("🗑️ Internship deleted with ID: " + id);
        } else {
            throw new RuntimeException("Internship ID not found: " + id);
        }
    }
}
