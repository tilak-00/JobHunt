package com.example.jobhunt.service;

import com.example.jobhunt.dto.JobDTO;
import com.example.jobhunt.entity.Job;
import com.example.jobhunt.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobRepository jobRepository;

    // ✅ Save a new job
    public void saveJob(JobDTO jobDTO) {
        logger.info("Saving job: {}", jobDTO);

        Job job = new Job();
        job.setCompany(jobDTO.getCompany());
        job.setRole(jobDTO.getRole());
        job.setEligibility(jobDTO.getEligibility());
        job.setQualification(jobDTO.getQualification());
        job.setSkills(jobDTO.getSkills());
        job.setExperience(jobDTO.getExperience());
        job.setSalary(jobDTO.getSalary());
        job.setLocation(jobDTO.getLocation());
        job.setApplyLink(jobDTO.getApplyLink());
        job.setMode(jobDTO.getMode());

        try {
            if (jobDTO.getLastDate() != null && !jobDTO.getLastDate().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate parsedDate = LocalDate.parse(jobDTO.getLastDate(), formatter);
                job.setLastDate(parsedDate);
            } else {
                job.setLastDate(null);
            }

            jobRepository.save(job);
            logger.info("✅ Job saved successfully in DB.");
        } catch (DateTimeParseException e) {
            logger.error("❌ Date parse error: {}", e.getMessage());
            throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd.");
        } catch (Exception e) {
            logger.error("❌ Failed to save job: {}", e.getMessage(), e);
            throw e;
        }
    }

    // ✅ Get all jobs
    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(job -> {
                    JobDTO dto = new JobDTO();
                    dto.setCompany(job.getCompany());
                    dto.setRole(job.getRole());
                    dto.setEligibility(job.getEligibility());
                    dto.setQualification(job.getQualification());
                    dto.setSkills(job.getSkills());
                    dto.setExperience(job.getExperience());
                    dto.setSalary(job.getSalary());
                    dto.setLocation(job.getLocation());
                    dto.setApplyLink(job.getApplyLink());
                    dto.setMode(job.getMode());
                    dto.setLastDate(job.getLastDate() != null ? job.getLastDate().toString() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ✅ Convert JobDTO to Job entity (used in Controller SSE)
    public Job convertDTOToEntity(JobDTO jobDTO) {
        Job job = new Job();
        job.setCompany(jobDTO.getCompany());
        job.setRole(jobDTO.getRole());
        job.setEligibility(jobDTO.getEligibility());
        job.setQualification(jobDTO.getQualification());
        job.setSkills(jobDTO.getSkills());
        job.setExperience(jobDTO.getExperience());
        job.setSalary(jobDTO.getSalary());
        job.setLocation(jobDTO.getLocation());
        job.setApplyLink(jobDTO.getApplyLink());
        job.setMode(jobDTO.getMode());
        if (jobDTO.getLastDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(jobDTO.getLastDate(), formatter);
            job.setLastDate(parsedDate);
        }
        return job;
    }

    // ✅ Delete a job by ID (used in Controller DELETE endpoint)
    public void deleteJobById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            logger.info("🗑️ Deleted job with ID: {}", id);
        } else {
            logger.warn("⚠️ Job ID {} not found. Nothing deleted.", id);
        }
    }

    // ✅ Scheduled cleanup: delete expired jobs (daily at midnight)
    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    public void deleteExpiredJobs() {
        LocalDate today = LocalDate.now();
        List<Job> expiredJobs = jobRepository.findByLastDateBefore(today);

        if (!expiredJobs.isEmpty()) {
            jobRepository.deleteAll(expiredJobs);
            logger.info("🧹 Deleted {} expired job(s).", expiredJobs.size());
        }
    }
}
