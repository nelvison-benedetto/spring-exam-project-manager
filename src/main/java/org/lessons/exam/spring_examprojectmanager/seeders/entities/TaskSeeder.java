package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskSeeder {

    private final ProjectSeeder projectSeeder;
    public final TaskRepo taskRepo;
    @Autowired
    public TaskSeeder(TaskRepo taskRepo, ProjectSeeder projectSeeder) {
        this.taskRepo = taskRepo;
        this.projectSeeder = projectSeeder;

    }   
    
    public void seed() {
        if(taskRepo.count() == 0){

            List<Project> projects = projectSeeder.seed();

            List<Task> tasks = new ArrayList<>(Arrays.asList(
                
                new Task(null, 
                    "Design UI Mockups", 
                    "Create mockups for the app redesign.",
                    "In_Progress", 
                    true, 
                    false, 
                    "High", 
                    LocalDate.of(2024, 5, 30), 
                    LocalDate.of(2024, 4, 1), 
                    LocalDate.of(2024, 5, 31), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(), 
                    projects.get(0),
                    new ArrayList<>()
                ),

                new Task(null, 
                    "Set Up Backend Environment", 
                    "Initialize the backend framework and architecture.",
                    "In_Review", 
                    true, 
                    false, 
                    "Medium", 
                    LocalDate.of(2024, 6, 10), 
                    LocalDate.of(2024, 5, 1), 
                    LocalDate.of(2024, 6, 9), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(), 
                    projects.get(1),
                    new ArrayList<>()
                ),

                new Task(null, 
                    "Implement Authentication", 
                    "Develop user authentication and authorization system.",
                    "Delayed", 
                    true, 
                    false, 
                    "High", 
                    LocalDate.of(2024, 6, 20), 
                    LocalDate.of(2024, 6, 1), 
                    LocalDate.of(2024, 6, 19), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(), 
                    projects.get(2),
                    new ArrayList<>()
                ),

                new Task(null, 
                    "Write Unit Tests", 
                    "Ensure code coverage for major components.",
                    "In_Test", 
                    true, 
                    false, 
                    "Low", 
                    LocalDate.of(2024, 7, 1), 
                    LocalDate.of(2024, 6, 15), 
                    LocalDate.of(2024, 6, 30), 
                    LocalDateTime.now(), 
                    LocalDateTime.now(), 
                    projects.get(3),
                    new ArrayList<>()
                )

            ));
            taskRepo.saveAll(tasks);
        }
    }
}
