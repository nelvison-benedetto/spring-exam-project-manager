package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter @Setter@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //link to user_id,..

    @Column(nullable = false)
    @NotBlank(message = "subscription type cannot be blank.")
    private String subscriptionType;

    @Column(nullable = false)
    @NotBlank(message = "status cannot be blank.")
    private String status;

    @Column(nullable = false)
    @NotNull(message = "subscription start date cannot be null.")
    private LocalDate subscriptionStartDate;  //@Temporal(TemporalType.DATE) x old versions 

    @Column(nullable = false)
    @NotNull(message = "subscription end date cannot be null.")
    private LocalDate subscriptionEndDate;

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

}
