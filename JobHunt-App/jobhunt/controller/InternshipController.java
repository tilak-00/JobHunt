package com.example.jobhunt.controller;

import com.example.jobhunt.dto.InternshipDTO;
import com.example.jobhunt.entity.Internship;
import com.example.jobhunt.service.InternshipService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/internships")
@CrossOrigin(origins = "*") // Allow frontend access
public class InternshipController {

    @Autowired
    private InternshipService internshipService;

    private static final List<SseEmitter> emitters = new ArrayList<>();

    // ✅ POST: Add new internship
    @PostMapping
    public ResponseEntity<String> addInternship(@RequestBody InternshipDTO internshipDTO) {
        try {
            String json = new ObjectMapper().writeValueAsString(internshipDTO);
            System.out.println("📥 Received Internship DTO: " + json);

            internshipService.saveInternship(internshipDTO);

            Internship internshipEntity = internshipService.convertDTOToEntity(internshipDTO);
            sendInternshipUpdate(internshipEntity);

            return ResponseEntity.ok("✅ Internship posted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to post internship: " + e.getMessage());
        }
    }

    // ✅ GET: Fetch all internships
    @GetMapping
    public ResponseEntity<List<InternshipDTO>> getAllInternships() {
        List<InternshipDTO> internships = internshipService.getAllInternships();
        return ResponseEntity.ok(internships);
    }

    // ✅ GET: SSE stream for real-time updates
    @GetMapping("/stream")
    public SseEmitter streamInternships() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // ✅ DELETE: Remove internship by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInternship(@PathVariable Long id) {
        try {
            internshipService.deleteInternshipById(id);
            return ResponseEntity.ok("🗑️ Internship deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to delete internship: " + e.getMessage());
        }
    }

    // 🔁 Send internship updates via SSE
    private void sendInternshipUpdate(Internship internship) {
        InternshipDTO dto = new InternshipDTO();
        dto.setCompany(internship.getCompany());
        dto.setRole(internship.getRole());
        dto.setEligibility(internship.getEligibility());
        dto.setQualification(internship.getQualification());
        dto.setSkills(internship.getSkills());
        dto.setExperience(internship.getExperience());
        dto.setStipend(internship.getStipend());
        dto.setLocation(internship.getLocation());
        dto.setApplyLink(internship.getApplyLink());
        dto.setLastDate(internship.getLastDate() != null ? internship.getLastDate().toString() : null);

        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(dto);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
