package com.joblisting.joblistingapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "jobs")
@Data
public class Job {
    @Id
    private String id;
    private String title;
    private String description;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String employerId;
    private List<String> applicants;

    public Job() {
        this.applicants = new ArrayList<>();
    }
}