package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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

    @Column(nullable = false)
    @NotBlank(message = "country cannot be blank.")
    private String country;

    @Override
    public String toString(){
        return String.format("%s %s %s", id, firstName, lastName);
    }

    //RELATIONS

    // @OneToOne(mappedBy = "person") // Relazione con User
    // private User user; // L'utente (relazione con l'entità User che è usata per la logica di sicurezza)

    // @ManyToMany(mappedBy = "persons") // Relazione con le aziende
    // private List<Company> companies = new ArrayList<>(); // Le aziende associate

    // @ManyToMany(mappedBy = "persons") // Relazione con i progetti
    // private List<Project> projects = new ArrayList<>(); // I progetti associati

    // @ManyToMany(mappedBy = "persons") // Relazione con i task
    // private List<Task> tasks = new ArrayList<>(); // I task associati

}
