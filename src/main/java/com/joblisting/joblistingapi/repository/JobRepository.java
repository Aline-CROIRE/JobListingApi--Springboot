package com.joblisting.joblistingapi.repository;

import com.joblisting.joblistingapi.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
}