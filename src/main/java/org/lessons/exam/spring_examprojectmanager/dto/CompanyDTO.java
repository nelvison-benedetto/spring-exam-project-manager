package org.lessons.exam.spring_examprojectmanager.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    private Integer id;
    private String companyLegalName;
    private String companyUsername;
    private String companyEIN;
    private String companyStateTaxID;
    private Integer clientId;
    private List<Integer> projectIds;
    private List<Integer> personIds;

    public CompanyDTO(Company company){  //constructor 1param
        this.id = company.getId();
        this.companyLegalName = company.getCompanyLegalName();
        this.companyUsername = company.getCompanyUsername();
        this.companyEIN = company.getCompanyEIN();
        this.companyStateTaxID = company.getCompanyStateTaxID();
        
        this.clientId = company.getClient() != null ? company.getClient().getId() : null;

        this.projectIds = company.getProjects().stream()
                                  .map(Project::getId)  //Project is old type-> getId() and get value -> set new item(that contains value) type value
                                  .collect(Collectors.toList());

        this.personIds = company.getPersons().stream()
                                 .map(Person::getId)
                                 .collect(Collectors.toList());
    }
    
}
