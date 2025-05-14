package org.lessons.exam.spring_examprojectmanager.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String country;
    private LocalDate birthdate;
    private Integer companyId;
    private Integer clientId;
    private List<Integer> projectIds;
    private List<Integer> messageIds;

    public PersonDTO(Person person){  //constructor 1param
        this.id = person.getId();
        this.firstname = person.getFirstname();
        this.lastname = person.getLastname();
        this.email = person.getEmail();
        this.phoneNumber = person.getPhoneNumber();
        this.country = person.getCountry();
        this.birthdate = person.getBirthdate();
        this.companyId = person.getCompany() != null ? person.getCompany().getId() : null;
        this.clientId = person.getClient() != null ? person.getClient().getId() : null;
        
        this.projectIds = person.getProjects().stream()
                                 .map(Project::getId)
                                 .collect(Collectors.toList());
        
        this.messageIds = person.getMessages().stream()
                                 .map(Message::getId)
                                 .collect(Collectors.toList());
    }
}
