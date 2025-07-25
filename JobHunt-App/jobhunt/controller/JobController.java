package com.example.jobhunt.controller;

import com.example.jobhunt.dto.JobDTO;
import com.example.jobhunt.entity.Job;
import com.example.jobhunt.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*") // Enable CORS for frontend apps
public class JobController {

    @Autowired
    private JobService jobService;

    private static final List<SseEmitter> emitters = new ArrayList<>();

    // ✅ Add new job
    @PostMapping
    public ResponseEntity<String> addJob(@RequestBody JobDTO jobDTO) {
        try {
            System.out.println("📥 Received Job DTO: " + jobDTO);
            jobService.saveJob(jobDTO);

            Job jobEntity = jobService.convertDTOToEntity(jobDTO);
            sendJobUpdate(jobEntity);

            return ResponseEntity.ok("✅ Job posted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to post job: " + e.getMessage());
        }
    }

    // ✅ Get all jobs
    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        List<JobDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // ✅ Server-Sent Events for real-time updates
    @GetMapping("/stream")
    public SseEmitter streamJobs() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // ✅ Delete a job by ID (Modified: uses jobService)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJobById(id);
            return ResponseEntity.ok().body("✅ Job deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to delete job: " + e.getMessage());
        }
    }

    // ✅ Push updates to all connected SSE clients
    public void sendJobUpdate(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setCompany(job.getCompany());
        jobDTO.setRole(job.getRole());
        jobDTO.setEligibility(job.getEligibility());
        jobDTO.setQualification(job.getQualification());
        jobDTO.setSkills(job.getSkills());
        jobDTO.setExperience(job.getExperience());
        jobDTO.setSalary(job.getSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setApplyLink(job.getApplyLink());
        jobDTO.setMode(job.getMode());
        jobDTO.setLastDate(job.getLastDate() != null ? job.getLastDate().toString() : null);

        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(jobDTO);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
