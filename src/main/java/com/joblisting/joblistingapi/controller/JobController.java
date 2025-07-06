package com.joblisting.joblistingapi.controller;

import com.joblisting.joblistingapi.model.Job;
import com.joblisting.joblistingapi.model.User;
import com.joblisting.joblistingapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        List<Job> jobs = jobService.findAllJobs();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "List of all jobs");
        response.put("jobs", jobs);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        Optional<Job> job = jobService.findJobById(id);
        Map<String, Object> response = new HashMap<>();
        if (job.isPresent()) {
            response.put("success", true);
            response.put("message", "Job found");
            response.put("job", job.get());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Job not found");
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String employerUsername = authentication.getName();

        Job newJob = jobService.addJob(job, employerUsername);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Job posted successfully");
        response.put("job", newJob);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<?> applyForJob(@PathVariable String jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String applicantUsername = authentication.getName();

        Map<String, Object> response = new HashMap<>();
        try {
            Job updatedJob = jobService.applyForJob(jobId, applicantUsername);
            response.put("success", true);
            response.put("message", "Applied to job successfully");
            response.put("job", updatedJob);
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{jobId}/applicants")
    public ResponseEntity<?> getJobApplicants(@PathVariable String jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String employerUsername = authentication.getName();
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> applicants = jobService.getApplicantsForJob(jobId, employerUsername);
            response.put("success", true);
            response.put("message", "Applicants fetched successfully");
            response.put("applicants", applicants);
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            response.put("success", false);
            response.put("message", "Access denied: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
