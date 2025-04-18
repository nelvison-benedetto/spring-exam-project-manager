package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class CompanyService {
    
    private final CompanyRepo companyRepo;
    private final ClientService clientService;
    private final ProjectService projectService;
    private final PersonService personService;
    @Autowired  //ora eseguito da Lombok w @RequiredArgsConstructor
    public CompanyService(CompanyRepo companyRepo, @Lazy ClientService clientService, @Lazy ProjectService projectService, PersonService personService){
        this.companyRepo = companyRepo;
        this.clientService = clientService;
        this.projectService = projectService;
        this.personService = personService;
    }
    

    public Boolean boolExistsById(Integer id){
        return companyRepo.existsById(id);
    }
    public Boolean boolExists(Company companyAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Company> companyOptional = companyRepo.findById(companyAskIfExists.getId());
        if(companyOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Company> optionalFindById(Integer id){
        return companyRepo.findById(id);
    }

    public Company checkedExistsById(Integer id){
        Optional<Company> companyOptional = companyRepo.findById(id);
        if(companyOptional.isPresent()){
            return companyOptional.get();
        }else{
            throw new ResourceNotFoundException("Company not found.");
        } 
    }

    //READ
    public List<Company> findAll(){
        return companyRepo.findAll();
    }

    public Company getById(Integer id){
        Company companyFound = checkedExistsById(id);
        return companyFound;
    }

    
    //CREATE
    public Company create(Company companyToCreate){
        if(companyToCreate == null){
            throw new IllegalArgumentException("Company to create cannot be null.");
        }
        if(companyRepo.existsByCompanyEIN(companyToCreate.getCompanyEIN())){
            throw new DuplicateResourceException("Company already exists for create.");
        }
        List<Person> validPersons = companyToCreate.getPersons().stream()  //LA COMPANY E' APPENA STATA CREATA, QUINDI L'UNICA PERSON LEGATA AD ESSO E LA PERSON APPENA CREATA IN SIGN-UP FLOW, ma poi in edit altre persons possono legarsi a questa company!!
            .map(person -> personService.checkedExistsById(person.getId()))
            .collect(Collectors.toList());  //.collect() put the results in a new collection, //.toSet() to specifi collection Set<>
        for(Person person : validPersons) {
            person.setCompany(companyToCreate);  //add this company to the obj person
        }
        companyToCreate.setPersons(validPersons);
        System.out.println("CreatedCompany...: " + companyToCreate);
        Company savedCompany = companyRepo.save(companyToCreate);
        System.out.println("Saved company from DB: " + companyRepo.findById(savedCompany.getId()).get()); //x debug GET DATA FROM THE DB!
        return savedCompany;
    }

    //UPDATE
    public Company edit(Company companyToEdit){
        if(companyToEdit == null){
            throw new IllegalArgumentException("Company to update cannot be null.");
        }
        Company existingCompany = checkedExistsById(companyToEdit.getId());
        
        existingCompany.setCompanyLegalName(companyToEdit.getCompanyLegalName());
        existingCompany.setCompanyUsername(companyToEdit.getCompanyUsername());
        existingCompany.setCompanyEIN(companyToEdit.getCompanyEIN());
        existingCompany.setCompanyStateTaxID(companyToEdit.getCompanyStateTaxID());


        List<Client> freshClients = existingCompany.getClients().stream()  //fresh upload from db
        .map(client -> clientService.checkedExistsById(client.getId())) 
        .toList();
        //reset all & overwrite!!
        existingCompany.getClients().clear(); 
        existingCompany.getClients().addAll(freshClients); 

        List<Project> freshProjects = companyToEdit.getProjects().stream()  //fresh upload from db
        .map(project -> projectService.checkedExistsById(project.getId())) 
        .toList();
        //reset all & overwrite!!
        existingCompany.getProjects().clear(); // Rimuovi tutte le associazioni progetti esistenti
        existingCompany.getProjects().addAll(freshProjects); // Aggiungi i nuovi progetti
        
        //List<Person> freshPersons = ... TO DO!!

        return companyRepo.save(existingCompany);
    }

    //DELETE
    public void delete(Company companyToDelete){
        if(companyToDelete == null){
            throw new IllegalArgumentException("Company to delete cannot be null.");
        }
        companyRepo.delete(companyToDelete);
    }
    public void deleteById(Integer id){
        Company companyToDelete = checkedExistsById(id);
        companyRepo.delete(companyToDelete);
    }
}
