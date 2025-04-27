package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.lessons.exam.spring_examprojectmanager.repository.PersonRepo;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    
    private final ProjectRepo projectRepo;
    private final CompanyService companyService;
    private final TaskService taskService;
    private final UserService userService;
    private final PersonRepo personRepo;
    private final PersonService personService;
    private final CompanyRepo companyRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, @Lazy CompanyService companyService, TaskService taskService, UserService userService,
    PersonRepo personRepo, PersonService personService, CompanyRepo companyRepo){
        this.projectRepo = projectRepo;
        this.companyService = companyService;
        this.taskService = taskService;
        this.userService = userService;
        this.personRepo = personRepo;
        this.personService = personService;
        this.companyRepo = companyRepo;
    }   
    

    public Boolean boolExistsById(Integer id){
        return projectRepo.existsById(id);
    }

    //also needs a projectAskIfExists w Id here!(no obj not yet saved to db so with Id null)
    public Boolean boolExists(Project projectAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Project> projectOptional = projectRepo.findById(projectAskIfExists.getId());
        if(projectOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Project> optionalFindById(Integer id){
        return projectRepo.findById(id);
    }

    public Project checkedExistsById(Integer id){
        Optional<Project> projectOptional = projectRepo.findById(id);
        if(projectOptional.isPresent()){
            return projectOptional.get();
        }else{
            throw new ResourceNotFoundException("Project not found.");
        } 
    }


    //READ
    @PreAuthorize("isAuthenticated()")
    public List<Project> findAll(){
        return projectRepo.findAll();
    }

    @PreAuthorize("@securityService.hasAccessToProject(#id, authentication)")
    public Project getById(Integer id){
        Project projectFound = checkedExistsById(id);
        return projectFound;
    }

    public Person checkPersonForActualUser(CustomUserDetails customUserDetails){
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            throw new RuntimeException("Person for this Security not found.");
        }
        return person;
    }


    @PreAuthorize("isAuthenticated()")
    public List<Project> securityGetAllProjects(CustomUserDetails customUserDetails){
        Person person = checkPersonForActualUser(customUserDetails);
        List<Project> projects;
        if(person.getCompany() != null) {
            return projects = findByCompaniesContaining(person.getCompany());
        } else { 
            return projects = findByPersonsContaining(person);
        }
    }

    @PreAuthorize("@securityService.hasAccessToProject(#id, authentication)")  //run method hasAccessToProject passing params id & customUserDetails logged, if returns true run the method below
    public Project securityGetSingleProject(Integer id, CustomUserDetails customUserDetails){
        Person person = checkPersonForActualUser(customUserDetails);
        Project project;
        if(person.getCompany() != null){
            project = findByIdAndCompaniesContaining(id, person.getCompany());
        }else{
            project = findByIdAndPersonsContaining(id, person);
        }
        return project;
    }


    //CREATE
    @PreAuthorize("isAuthenticated()")
    public Project create(Project projectToCreate, CustomUserDetails customUserDetails){
        if(projectToCreate == null){
            throw new IllegalArgumentException("Project to create cannot be null.");
        }
        Person person = checkPersonForActualUser(customUserDetails);
        if(person.getCompany() != null){
            Company company = person.getCompany();
            projectToCreate.getCompanies().add(company);
            company.getProjects().add(projectToCreate);
        }
        else{
            projectToCreate.getPersons().add(person);   //SETTO DA ENTRAMBI I LATI...
            person.getProjects().add(projectToCreate);   //SETTO DA ENTRAMBI I LATI...
        }
        Project savedProject = projectRepo.save(projectToCreate);  //E POI SOLO DOPO SALVO!
        System.out.println("Saved project from DB: " + projectRepo.findById(savedProject.getId()).get());   
        return savedProject;
    }

    //UPDATE
    @PreAuthorize("@securityService.hasAccessToProject(#projectToEdit.id, authentication)")
    public Project edit(Project projectToEdit, CustomUserDetails customUserDetails){
        if(projectToEdit == null){
            throw new IllegalArgumentException("Project to update cannot be null.");
        }
        Project existingProject = securityGetSingleProject(projectToEdit.getId(), customUserDetails);
        //!!x security(choose exactly which fields change) & x help spring persistenza(Spring Data/JPA) Edit always by setting fields one at a time!
        
        existingProject.setTitle(projectToEdit.getTitle());
        existingProject.setDescription(projectToEdit.getDescription());
        existingProject.setStatus(projectToEdit.getStatus());
        existingProject.setIsActive(projectToEdit.getIsActive());
        existingProject.setIsCompleted(projectToEdit.getIsCompleted());
        existingProject.setCategory(projectToEdit.getCategory());
        existingProject.setBudget(projectToEdit.getBudget());
        existingProject.setPriority(projectToEdit.getPriority());
        existingProject.setDueDate(projectToEdit.getDueDate());
        existingProject.setProjectStartDate(projectToEdit.getProjectStartDate());
        existingProject.setProjectEndDate(projectToEdit.getProjectEndDate());
        

        //fresh upload from db to avoid errors with incomplete objects
        List<Company> freshCompanies = existingProject.getCompanies().stream()   //use existingProject, because projectToEdit is coming from a form(no field tasks)
        .map(c -> companyService.checkedExistsById(c.getId())).toList();    
        existingProject.getCompanies().clear();  //rimuovi tutte le associazioni attuali
        existingProject.getCompanies().addAll(freshCompanies);  //aggiungi quelle nuove

        //fresh upload from db
        List<Person> freshPersons = existingProject.getPersons().stream()   //use existingProject, because projectToEdit is coming from a form(no field tasks)
        .map(c -> personService.checkedExistsById(c.getId())).toList();
        existingProject.getPersons().clear();
        existingProject.getPersons().addAll(freshPersons);

        //RELATION MANY-ONE  tasks-project
        //fresh upload from db
        List<Task> freshTasks = existingProject.getTasks().stream()  
        .map(c -> taskService.checkedExistsById(c.getId())).toList();
        //reset all & overwrite
        existingProject.getTasks().clear();
        for(Task task : freshTasks){  //!!x RELATION MANY-ONE add THIS to who has LIST<> (cioe in rel project-tasks add here)
            task.setProject(existingProject); //!important Imposta la relazione sul lato owner
        }
        //System.out.println("freshTasks" + freshTasks);
        existingProject.getTasks().addAll(freshTasks);
        //System.out.println("advanced existingProject tasks" + existingProject.getTasks());

        Project savedProject = projectRepo.save(existingProject);
        System.out.println("Saved project from DB: " + projectRepo.findById(savedProject.getId()).get());
        return savedProject;
    }

    //DELETE
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToProject(#projectToDelete.id, authentication)")
    public void delete(Project projectToDelete, CustomUserDetails customUserDetails){
        if(projectToDelete == null){
            throw new IllegalArgumentException("Project to delete cannot be null.");
        }
        Person person = checkPersonForActualUser(customUserDetails);  //already thrown excp id doesn't find existing person
        projectRepo.delete(projectToDelete);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToProject(#id, authentication)")
    public void deleteById(Integer id, CustomUserDetails customUserDetails){
        //Project projectToDelete = checkedExistsById(id);
        Project project = securityGetSingleProject(id, customUserDetails);
        
        for(Company company : project.getCompanies()){
            company.getProjects().remove(project);
            companyRepo.save(company);
        }
        project.getCompanies().clear();

        for(Task task : project.getTasks()){
            task.setProject(null);
            //taskService.edit(task);
            //TO FINISH
        }
        project.getTasks().clear();

        for(Person person : project.getPersons()){
            person.getProjects().remove(project);
            personRepo.save(person);
        }
        project.getPersons().clear();
        
        projectRepo.save(project);
        projectRepo.delete(project);
    }


    //FILTERS

    public List<Project> findByPersonsContaining(Person person){
        return projectRepo.findByPersonsContaining(person);
    }

    public List<Project> findByCompaniesContaining(Company company){
        return projectRepo.findByCompaniesContaining(company);
    }

    public Project findByIdAndPersonsContaining(Integer projectId, Person person){
        Project project = projectRepo.findByIdAndPersonsContaining(projectId, person);
        if(project == null) {
            throw new AccessDeniedException("You do not have access to this project.");
        }
        return project;
    }
    public Project findByIdAndCompaniesContaining(Integer projectId, Company company){
        Project project = projectRepo.findByIdAndCompaniesContaining(projectId, company);
        if(project == null) {
            throw new AccessDeniedException("You do not have access to this project.");
        }
        return project;
    }

}
