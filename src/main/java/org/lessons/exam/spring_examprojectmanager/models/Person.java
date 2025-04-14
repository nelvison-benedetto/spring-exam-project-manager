package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "last name cannot be blank.")
    private String lastname;

    @Column(nullable = false)
    @NotBlank(message = "username cannot be blank.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "email cannot be blank.")
    @Email(message = "email invalid format.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "phone number cannot be blank.")
    private String phoneNumber = "+00";  //w String phonenumber can start w 0!(otherwise w Integer e.g.04664566 would not be saved)

    @Column(nullable = false)
    @NotBlank(message = "country cannot be blank.")
    private String country;

    @Column(nullable = false)
    @NotNull(message = "birthdate cannot be null.")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthdate;

    @Override
    public String toString(){
        return String.format("%s %s %s %s %s %s %s", id, firstname, lastname, username, email, phoneNumber, country, birthdate);
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
