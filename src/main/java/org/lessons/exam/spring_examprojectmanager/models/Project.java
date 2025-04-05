package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @NotBlank(message = "status cannot be blank.")
    private String status; 

    @Column(nullable = false)
    @NotNull(message = "is active cannot be null.")
    private boolean isActive;

    @Column(nullable = false)
    @NotNull(message = "is completed cannot be null.")
    private boolean isCompleted;

    @Column(nullable = false)
    @NotBlank(message = "category cannot be blank.")
    private String category;

    @Column(nullable = false)
    @NotNull(message = "budget cannot be null.")
    private BigDecimal budget;

    @Column(nullable = false)
    @NotBlank(message = "priority cannot be blank.")
    private String priority;

    @Column(nullable = false)
    @NotNull(message = "due date cannot be null.")
    private LocalDate dueDate;

    @Column(nullable = false)
    @NotNull(message = "project start date cannot be null.")
    private LocalDate projectStartDate;  //@Temporal(TemporalType.DATE) x old versions 

    @Column(nullable = false)
    @NotNull(message = "project end date cannot be null.")
    private LocalDate projectEndDate;


    
    @Column(nullable = false)
    @NotNull(message = "created at cannot be null.")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @NotNull(message = "updated at cannot be null.")
    private LocalDateTime updatedAt;

    @PrePersist  //called before save on db for the first time
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
        return String.format("%s %s %s %s", id, title, description, status);
    }


    //RELATIONS
    @ManyToMany(mappedBy = "projects")
    @JsonBackReference
    private List<Company> companies = new ArrayList<>();


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    
}
