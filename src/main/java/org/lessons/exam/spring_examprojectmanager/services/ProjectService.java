package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    
    private final ProjectRepo projectRepo;
    private final CompanyService companyService;
    private final TaskService taskService;
    @Autowired
    public ProjectService(ProjectRepo projectRepo, @Lazy CompanyService companyService, TaskService taskService){
        this.projectRepo = projectRepo;
        this.companyService = companyService;
        this.taskService = taskService;
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
    public List<Project> findAll(){
        return projectRepo.findAll();
    }

    public Project getById(Integer id){
        Project projectFound = checkedExistsById(id);
        return projectFound;
    }


    //CREATE
    public Project create(Project projectToCreate){
        if(projectToCreate == null){
            throw new IllegalArgumentException("Project to create cannot be null.");
        }
        return projectRepo.save(projectToCreate);
    }

    //UPDATE
    public Project edit(Project projectToEdit){
        if(projectToEdit == null){
            throw new IllegalArgumentException("Project to update cannot be null.");
        }
        Project existingProject = checkedExistsById(projectToEdit.getId());
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
        .map(c -> companyService.checkedExistsById(c.getId()))
        .toList();
        //reset all & overwrite (relation MANY-TO-MANY)!!
        existingProject.getCompanies().clear();  //rimuovi tutte le associazioni attuali
        existingProject.getCompanies().addAll(freshCompanies);  //aggiungi quelle nuove

        //RELATION MANY-ONE  tasks-project
        //fresh upload from db
        List<Task> freshTasks = existingProject.getTasks().stream()  
        .map(c -> taskService.checkedExistsById(c.getId()))
        .toList();
        //reset all & overwrite
        existingProject.getTasks().clear();
        for (Task task : freshTasks){  //!!x RELATION MANY-ONE add THIS to who has LIST<> (cioe in rel project-tasks add here)
            task.setProject(existingProject); //!important Imposta la relazione sul lato owner
        }
        System.out.println("freshTasks" + freshTasks);
        existingProject.getTasks().addAll(freshTasks);
        System.out.println("advanced existingProject" + existingProject);

        return projectRepo.save(existingProject);
    }

    //DELETE
    public void delete(Project projectToDelete){
        if(projectToDelete == null){
            throw new IllegalArgumentException("Project to delete cannot be null.");
        }
        projectRepo.delete(projectToDelete);
    }

    public void deleteById(Integer id){
        Project projectToDelete = checkedExistsById(id);
        projectRepo.delete(projectToDelete);
    }
}
