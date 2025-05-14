package org.lessons.exam.spring_examprojectmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.lessons.exam.spring_examprojectmanager.models.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Boolean isActive;
    private Boolean isCompleted;
    private String category;
    private BigDecimal budget;
    private String priority;
    private LocalDate dueDate;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectDTO(Project project) {  //constructor 1param
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.status = project.getStatus();
        this.isActive = project.getIsActive();
        this.isCompleted = project.getIsCompleted();
        this.category = project.getCategory();
        this.budget = project.getBudget();
        this.priority = project.getPriority();
        this.dueDate = project.getDueDate();
        this.projectStartDate = project.getProjectStartDate();
        this.projectEndDate = project.getProjectEndDate();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
}
