package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.lessons.exam.spring_examprojectmanager.repository.PersonRepo;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    
    private final PersonRepo personRepo;
    private final UserService userService;
    private final ProjectService projectService;
    private final CompanyService companyService;
    private final ClientService clientService;
    private final ProjectRepo projectRepo;
    private final CompanyRepo companyRepo;

    @Autowired 
    public PersonService(PersonRepo personRepo, UserService userService, @Lazy ProjectService projectService, CompanyService companyService, ClientService clientService, ProjectRepo projectRepo, CompanyRepo companyRepo) {
        this.personRepo = personRepo;
        this.userService = userService;
        this.projectService = projectService;
        this.companyService = companyService;
        this.clientService = clientService;
        this.projectRepo = projectRepo;
        this.companyRepo = companyRepo;
    }


    public Boolean boolExistsById(Integer id){
        return personRepo.existsById(id);
    }

    public Boolean boolExists(Person personAskIfExists){
        Optional<Person> personOptional = personRepo.findById(personAskIfExists.getId());
        if(personOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Person> optionalFindById(Integer id){
        return personRepo.findById(id);
    }

    public Person checkedExistsById(Integer id){
        Optional<Person> personOptional = personRepo.findById(id);
        if(personOptional.isPresent()){
            return personOptional.get();
        }else{
            throw new ResourceNotFoundException("Person not found.");
        }
    }

    
    //READ

    @PreAuthorize("isAuthenticated()")
    public Person getByIdNoSecMain(Integer id){
        Person personFound = getById(id);
        return personFound;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Person> findAll(){
        return personRepo.findAll();
    }

    //@PreAuthorize("@securityService.hasAccessToPerson(#id, authentication)")  //removed so persons(auth but not main) can see persons/{id} page !
    @PreAuthorize("isAuthenticated()")
    public Person getById(Integer id){
        Person personFound = checkedExistsById(id);
        return personFound;
    }

    public Person checkPersonForActualUser(CustomUserDetails customUserDetails){
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            throw new RuntimeException("Person for this Security not found.");
        }
        return person;
    }


    @PreAuthorize("isAuthenticated()")
    public List<Person> securityGetAllPersons(CustomUserDetails customUserDetails){
        //Person person = checkPersonForActualUser(customUserDetails);
        List<Person> persons = personRepo.findAll();
        return persons;
        //no necessary sec to get all of them
    }

    @PreAuthorize("@securityService.hasAccessToPerson(#id, authentication)")  //run method hasAccessToProject passing params id & customUserDetails logged, if returns true run the method below
    public Person securityGetSinglePerson(Integer id, CustomUserDetails customUserDetails){
        Person personTarget = getById(id);
        return personTarget;
        //already checked sec in securityServ
    }
    


    //CREATE
    public Person create(Person personToCreate){
        if(personToCreate == null || personToCreate.getUser() == null){
            throw new IllegalArgumentException("Person or associated User cannot benull.");
        }
        User fullUser = userService.checkedExistsById(personToCreate.getUser().getId());
        if (fullUser.getPerson() != null) {
            throw new IllegalStateException("User already linked to a Person.");
        }
        personToCreate.setUser(fullUser);
        fullUser.setPerson(personToCreate);  //!important to manually set also this x help java!

        System.out.println("CreatedPerson...: " + personToCreate);
        Person savedPerson = personRepo.save(personToCreate);
        System.out.println("Saved person from DB: " + personRepo.findById(savedPerson.getId()).get()); //x debug GET DATA FROM THE DB!
        return savedPerson;
    }

    //UPDATE
    @PreAuthorize("@securityService.hasAccessToPerson(#personToEdit.id, authentication)")
    public Person edit(Person personToEdit, CustomUserDetails customUserDetails){
        if(personToEdit == null){
            throw new IllegalArgumentException("Person to update cannot be null.");
        }
        Person existingPerson = checkedExistsById(personToEdit.getId());

        //update one field at a time!(x security & x help Spring)
        existingPerson.setFirstname(personToEdit.getFirstname());
        existingPerson.setLastname(personToEdit.getLastname());
        existingPerson.setEmail(personToEdit.getEmail());
        existingPerson.setPhoneNumber(personToEdit.getPhoneNumber());
        existingPerson.setCountry(personToEdit.getCountry());
        existingPerson.setBirthdate(personToEdit.getBirthdate());

        //refresh all
        User freshUser = userService.checkedExistsById(personToEdit.getUser().getId());
        existingPerson.setUser(freshUser);
        freshUser.setPerson(existingPerson);

        if(personToEdit.getCompany() != null){
            Company freshCompany = companyService.checkedExistsById(personToEdit.getCompany().getId());
            existingPerson.setCompany(freshCompany);
            freshCompany.getPersons().add(existingPerson);
        }

        if(personToEdit.getClient() != null){
            Client freshClient = clientService.checkedExistsById(personToEdit.getClient().getId());
            existingPerson.setClient(freshClient);
            freshClient.setPerson(existingPerson);
        }

        List<Project> freshProjects = personToEdit.getProjects().stream()  //fresh upload from db
        .map(project -> projectService.checkedExistsById(project.getId())) 
        .toList();
        for(Project oldProject : existingPerson.getProjects()){
            oldProject.getPersons().remove(existingPerson);  //remove this person from any single old proj
        }
        existingPerson.getProjects().clear();  //clear all old projects list
        existingPerson.getProjects().addAll(freshProjects);  //reassign
        for (Project project : freshProjects){
            if (!project.getPersons().contains(existingPerson)){
                project.getPersons().add(existingPerson);  //reassign
            }
        }
        //TODO check if works
        existingPerson.getMessages().clear();
        if(personToEdit.getMessages() != null) {
            existingPerson.getMessages().addAll(personToEdit.getMessages());
        }

        return personRepo.save(existingPerson);
    }

    
    //DELETE
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToPerson(#personToDelete.id, authentication)")
    public void delete(Person personToDelete){
        if(personToDelete == null){
            throw new IllegalArgumentException("Person to delete cannot be null.");
        }
        personRepo.delete(personToDelete);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToPerson(#id, authentication)")
    public void deleteById(Integer id){
        Person personToDelete = checkedExistsById(id);
        //IF U WANT DELETE ACCOUNT(cascade delete person->company->client->projects->task) do it from 'user delete'
        personRepo.delete(personToDelete);
    }


    //OTHERS
    @PreAuthorize("isAuthenticated()")
    public List<Person> personsFindAllLessMain(CustomUserDetails customUserDetails){
        Person person = checkPersonForActualUser(customUserDetails);
        List<Person> persons = findAll()
            .stream()
            .filter(p -> ! p.getId().equals(person.getId())).toList();
        return persons;
    }
    @PreAuthorize("isAuthenticated()")
    public List<Person> personsFindAllLessMainLessWithoutCompany(CustomUserDetails customUserDetails){
        Person person = checkPersonForActualUser(customUserDetails);
        List<Person> persons = findAll()
            .stream()
            .filter(p -> ! p.getId().equals(person.getId()) && p.getCompany() == null).toList();
        return persons;
    }


    @PreAuthorize("isAuthenticated()")
    public List<Person> personsSearchByForm(String email, String phoneNumber, CustomUserDetails customUserDetails ){
        Person person = checkPersonForActualUser(customUserDetails);

        boolean hasEmail = email != null && !email.isBlank();
        boolean hasPhone = phoneNumber != null && !phoneNumber.isBlank();

        if (hasEmail && hasPhone) {
            return personRepo.findByEmailContainingAndPhoneNumberContainingAndIdNot(email, phoneNumber, person.getId());
        } else if (hasEmail) {
            return personRepo.findByEmailContainingAndIdNot(email, person.getId());
        } else if (hasPhone) {
            return personRepo.findByPhoneNumberContainingAndIdNot(phoneNumber, person.getId());
        } else {
            return personRepo.findByIdNot(person.getId());
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void personsAssociatePersonToProject(Integer projectId, Integer personId) {

        Project project = projectService.getByIdNoSecMain(projectId);
        Person person = getByIdNoSecMain(personId);

        if (project != null && person != null) {
            if (!person.getProjects().contains(project)) {
                person.getProjects().add(project);
            }
        
            if (!project.getPersons().contains(person)) {
                project.getPersons().add(person);
            }
            personRepo.save(person);
            projectRepo.save(project);
        }else{
            System.out.println("Project or Person .getByIdNoSecMain() not found.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void personsAssociatePersonToCompany(Integer companyId, Integer personId){

        Company company = companyService.getByIdNoSecMain(companyId);
        Person person = getByIdNoSecMain(personId);

        if(company != null && person != null){
            if(person.getCompany() == null && !company.getPersons().contains(person)){
                person.setCompany(company);
                company.getPersons().add(person);
                personRepo.save(person);
            }else{
                System.out.println("Person target already has a company associated || company target has laredy associated this person.");
            }
        }else{
            System.out.println("Company or Person .getByIdNoSecMain() not found.");
        }
    }


}
