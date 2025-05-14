package org.lessons.exam.spring_examprojectmanager.dto;

import java.time.LocalDate;

import org.lessons.exam.spring_examprojectmanager.models.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Boolean isActive;
    private Boolean isCompleted;
    private String priority;
    private LocalDate dueDate;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private String projectTitle;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.isActive = task.getIsActive();
        this.isCompleted = task.getIsCompleted();
        this.priority = task.getPriority();
        this.dueDate = task.getDueDate();
        this.taskStartDate = task.getTaskStartDate();
        this.taskEndDate = task.getTaskEndDate();
        
        this.projectTitle = task.getProject() != null ? task.getProject().getTitle() : null;  //PROJECT E' CARICATO IN MOD 'LAZY', e se non è ancora stato serializzato spring si blocca qui!!
    }
}
