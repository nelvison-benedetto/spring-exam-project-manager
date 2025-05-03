package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.time.LocalDate;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persons")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "first name cannot be blank.")
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "last name cannot be blank.")
    private String lastname;

    //username already setted in sign-up form
    // @Column(nullable = false)
    // @NotBlank(message = "username cannot be blank.")
    // private String username;

    @Column(nullable = false)
    @NotBlank(message = "email cannot be blank.")
    @Email(message = "email invalid format.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "phone number cannot be blank.")
    private String phoneNumber ;  //w String phonenumber can start w 0!(otherwise w Integer e.g.04664566 would not be saved)

    @Column(nullable = false)
    @NotBlank(message = "country cannot be blank.")
    private String country;

    @Column(nullable = false)
    @NotNull(message = "birthdate cannot be null.")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthdate;

    @Override
    public String toString(){
        return String.format("%s %s %s %s %s %s", id, firstname, lastname, email, phoneNumber, country, birthdate);
    }

    
    //RELATIONS

    @OneToOne(mappedBy = "person", orphanRemoval = true, fetch = FetchType.LAZY)
    //@NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id")
    @JsonManagedReference
    private Company company;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Client client;



    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
        name = "person_project",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonManagedReference
    private List<Project> projects = new ArrayList<>();

    
    //DISCONNECTIONS BEFORE DELETES, better do it manually!!!
    // @PreRemove
    // private void removeProjectsAssociation(){  //add this where you have @jointable(name,joincolumns,inversejoincolumns)
    //     for(Project project : projects){
    //         project.getPersons().remove(this);  //disconnect this person from each project
    //     }
    //     projects.clear();  //disconnect all projects from this person
    // }
}
