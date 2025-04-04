package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "first name cannot be blank.")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "last name cannot be blank.")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "email cannot be blank.")
    @Email(message = "email invalid format.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "phone number cannot be blank.")
    private String phoneNumber;  //can start w 0


}
