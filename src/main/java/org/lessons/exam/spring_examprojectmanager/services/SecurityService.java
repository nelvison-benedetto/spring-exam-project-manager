package org.lessons.exam.spring_examprojectmanager.services;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.ClientRepo;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.lessons.exam.spring_examprojectmanager.repository.MessageRepo;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("securityService")  //securityService x call it easily in the @PreAuthorize
public class SecurityService {
    
    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final TaskRepo taskRepo;
    private final CompanyRepo companyRepo;
    private final ClientRepo clientRepo;
    private final MessageRepo messageRepo;

    @Autowired
    public SecurityService(ProjectRepo projectRepo, UserService userService, TaskRepo taskRepo, CompanyRepo companyRepo, ClientRepo clientRepo, MessageRepo messageRepo){
        this.projectRepo = projectRepo;
        this.userService = userService;
        this.taskRepo = taskRepo;
        this.companyRepo = companyRepo;
        this.clientRepo = clientRepo;
        this.messageRepo = messageRepo;
    }


    public Person checkPersonForActualUser(CustomUserDetails customUserDetails){
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            throw new RuntimeException("Person for this Security not found.");
        }
        return person;
    }

    //x Persons
    public Boolean hasAccessToPerson(Integer personId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {  //CHECK if principal type == type CustomUserDetails, + assegnazione a customUserDetails
            return false;
        }
        Person authenticatedPerson = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (authenticatedPerson == null) {
            return false;
        }
        return authenticatedPerson.getId().equals(personId);
    }

    //x Projects
    public Boolean hasAccessToProject(Integer projectId, Authentication authentication) {  //correct is org.springframework.security.core.Authentication;
        if(authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if(!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            return false;
        }
        Project project = projectRepo.findById(projectId).orElse(null);
        return project != null && ( project.getPersons().contains(person) || project.getCompanies().contains(person.getCompany()));
    }

    //x Tasks
    public Boolean hasAccessToTask(Integer taskId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (person == null) {
            return false;
        }
        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getProject() == null) {
            return false;
        }
        Project project = task.getProject();
        return project != null && ( project.getPersons().contains(person) || project.getCompanies().contains(person.getCompany()));
    }

    //x Company
    public Boolean hasAccessToCompany(Integer companyId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (person == null) {
            return false;
        }
        Company company = companyRepo.findById(companyId).orElse(null);
        if (company == null) {
            return false;
        }
        return company.getPersons().contains(person); // or company.getEmployees().contains(person)
    }

    //x Client
    public Boolean hasAccessToClient(Integer clientId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (person == null) {
            return false;
        }
        Client client = clientRepo.findById(clientId).orElse(null);
        if (client == null) {
            return false;
        }
        if ( client.getCompany() == null && client.getPerson() == null){
            return false;
        }

        //TODO TO IMPROVE HERE!!
        if(client.getCompany() != null){
            return client.getCompany().getPersons().contains(person);
        }else{
            return client.getPerson().equals(person);
        }
        
    }

    // x Message
    public Boolean hasAccessToMessage(Integer messageId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (person == null) {
            return false;
        }
        Message message = messageRepo.findById(messageId).orElse(null);
        if (message == null || message.getTask() == null || message.getTask().getProject() == null) {
            return false;
        }
        Project project = message.getTask().getProject();
        return project.getPersons().contains(person) || project.getCompanies().contains(person.getCompany());
    }


    
}
