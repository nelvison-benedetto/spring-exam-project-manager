package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "title cannot be blank.")
    private String title;

    @Column(nullable = false)
    @Size(max = 2000, message = "description cannot be longer than 2000 characters.")
    @NotBlank(message = "description cannot be blank.")
    private String description;




    @Column(nullable = false)
    @NotBlank(message = "status cannot be null.")
    private String status; 

    @Column(nullable = false)
    @NotNull(message = "is active cannot be null.")
    private Boolean isActive = false;

    @Column(nullable = false)
    @NotNull(message = "is completed cannot be null.")
    private Boolean isCompleted = false;

    @Column(nullable = false)
    @NotBlank(message = "category cannot be blank.")
    private String category;

    @Column(nullable = false)
    @NotNull(message = "budget cannot be blank.")
    private BigDecimal budget;  // = new BigDecimal("0.00"); default, but now return correctly also w a blank

    @Column(nullable = false)
    @NotBlank(message = "priority cannot be blank.")
    private String priority = "Low";

    @Column(nullable = false)
    @NotNull(message = "due date cannot be null.")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dueDate;

    @Column(nullable = false)
    @NotNull(message = "project start date cannot be null.")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate projectStartDate;  //@Temporal(TemporalType.DATE) x old versions 

    @Column(nullable = false)
    @NotNull(message = "project end date cannot be null.")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate projectEndDate;


    
    @Column
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @PrePersist  //called before obj Project saved on db for the first time, then set cols createdAt & updatedAt
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    @PreUpdate  //called before save the updates on db
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString(){
        return String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s", id, title, description, status, isActive, isCompleted, category, budget, priority, dueDate, projectStartDate, projectEndDate, tasks);
    }


    //RELATIONS
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Company> companies = new ArrayList<>();


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    
}
