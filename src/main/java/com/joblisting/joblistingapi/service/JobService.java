package com.joblisting.joblistingapi.service;

import com.joblisting.joblistingapi.model.Job;
import com.joblisting.joblistingapi.model.User;
import com.joblisting.joblistingapi.repository.JobRepository;
import com.joblisting.joblistingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> findJobById(String id) {
        return jobRepository.findById(id);
    }

    public Job addJob(Job job, String employerUsername) {
        User employer = userRepository.findByUsername(employerUsername)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        job.setEmployerId(employer.getId());
        return jobRepository.save(job);
    }

    public Job applyForJob(String jobId, String applicantUsername) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        User applicant = userRepository.findByUsername(applicantUsername)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (!job.getApplicants().contains(applicant.getId())) {
            job.getApplicants().add(applicant.getId());
        }
        return jobRepository.save(job);
    }

    public List<User> getApplicantsForJob(String jobId, String employerUsername) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        User employer = userRepository.findByUsername(employerUsername)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (!job.getEmployerId().equals(employer.getId())) {
            throw new SecurityException("You are not authorized to view applicants for this job");
        }
        return userRepository.findAllById(job.getApplicants());
    }
}