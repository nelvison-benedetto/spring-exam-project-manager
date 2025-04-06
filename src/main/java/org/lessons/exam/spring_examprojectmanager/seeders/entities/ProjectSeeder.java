package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectSeeder {
    
    private final ProjectRepo projectRepo;
    @Autowired
    public ProjectSeeder(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }   

    public List<Project> generatedProjects(){
        return Arrays.asList(
            
            new Project(
                    null, 
                    "TechNova App Redesign", 
                    "Redesigning the mobile app for TechNova.", 
                    "IN_PROGRESS", 
                    true, 
                    false, 
                    "Mobile Development", 
                    new BigDecimal("50000.00"), 
                    "High", 
                    LocalDate.of(2024, 6, 15), 
                    LocalDate.of(2024, 2, 1), 
                    LocalDate.of(2024, 12, 1), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    new ArrayList<>()
                ),
    
                new Project(
                    null, 
                    "GreenByte Marketing Campaign", 
                    "Marketing campaign for GreenByte's new product launch.", 
                    "COMPLETED", 
                    false, 
                    true, 
                    "Marketing", 
                    new BigDecimal("30000.00"), 
                    "Medium", 
                    LocalDate.of(2024, 7, 1), 
                    LocalDate.of(2024, 3, 1), 
                    LocalDate.of(2024, 8, 30), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    new ArrayList<>()
                ),
    
                new Project(
                    null, 
                    "GreenByte Web Development", 
                    "Developing a new website for GreenByte.", 
                    "IN_PROGRESS", 
                    true, 
                    false, 
                    "Web Development", 
                    new BigDecimal("40000.00"), 
                    "Medium", 
                    LocalDate.of(2024, 5, 1), 
                    LocalDate.of(2024, 3, 1), 
                    LocalDate.of(2024, 11, 1), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    new ArrayList<>()
                ),
    
                new Project(
                    null, 
                    "TechNova Cloud Infrastructure", 
                    "Setting up cloud infrastructure for TechNova.", 
                    "PENDING", 
                    true, 
                    false, 
                    "Infrastructure", 
                    new BigDecimal("70000.00"), 
                    "High", 
                    LocalDate.of(2024, 10, 1), 
                    LocalDate.of(2024, 5, 1), 
                    LocalDate.of(2025, 1, 1), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    new ArrayList<>()
                )
        );
    }

    public List<Project> seed() {
        if(projectRepo.count() == 0) {
            List<Project> projects = generatedProjects();
            return projectRepo.saveAll(projects);
        }
        return projectRepo.findAll();
    }

}
