package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="companies")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //il db genera auto l'id + AUTO_INCREMENT per la column
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "company legal name cannot be blank.")
    private String companyLegalName;

    @Column(nullable = false)
    @NotBlank(message = "company username cannot be blank.")
    private String companyUsername;

    @Column(nullable = false)
    @NotBlank
    //@Size(min = 9, max = 9, message = "company EIN must contain exactly 9 digits.")
    //@Pattern(regexp = "\\d{2}-\\d{7}", message = "Company EIN must follow the format XX-XXXXXXX.")
    private String companyEIN;  //XX-XXXXXXX

    @Column(nullable = false)
    @NotBlank
    //@Pattern(regexp = "\\d{3}-\\d{4}-\\d", message = "Company State Tax ID must follow the format XXX-XXXX-X")
    private String companyStateTaxID;
    
    @Override
    public String toString(){
        return String.format("%s %s %s %s %s %s", id, companyLegalName, companyUsername, companyEIN, companyStateTaxID, client, projects);
    }

    
    //RELATIONS
    
    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Client client;
    

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)  
    @JoinTable(
        name = "company_project",  //nome tab ponte (sempre in ordine alfabetico)
        joinColumns = @JoinColumn(name = "company_id"),  //FK lato Company, aggiornato quando save/update/delete old/new company (check also Cascade ect)
        inverseJoinColumns = @JoinColumn(name = "project_id")  //FK lato Project, aggiornato quando save/update/delete old/new project (check also Cascade ect)
    )
    @JsonManagedReference
    private List<Project> projects = new ArrayList<>();


    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Person> persons = new ArrayList<>();


    // //DISCONNECTIONS BEFORE DELETE,  //THIS ISN'T SUGGESTED BECAUSE THE DELETED IS ALWAYS COMPLICATED TO MANAGE, manage it manually!
    // @PreRemove
    // private void removeProjectsAssociation() {  //add this where you have @jointable(name,joincolumns,inversejoincolumns)
    //     for(Project project : projects){
    //         project.getCompanies().remove(this);  //disconnect this company from each project
    //     }
    //     projects.clear();  //disconnect all projects from this company
    // }
}



