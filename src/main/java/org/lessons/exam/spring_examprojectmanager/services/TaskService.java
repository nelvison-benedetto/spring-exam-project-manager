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
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    
    private final TaskRepo taskRepo;
    private final SecurityService securityService;
    private final ProjectRepo projectRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, SecurityService securityService, ProjectRepo projectRepo) {
        this.taskRepo = taskRepo;
        this.securityService = securityService;
        this.projectRepo = projectRepo;
    }

    public Boolean boolExistsById(Integer id){
        return taskRepo.existsById(id);
    }

    public Boolean boolExists(Task taskAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Task> taskOptional = taskRepo.findById(taskAskIfExists.getId());
        if(taskOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Task> optionalFindById(Integer id){
        return taskRepo.findById(id);
    }

    public Task checkedExistsById(Integer id){
        Optional<Task> taskOptional = taskRepo.findById(id);
        if(taskOptional.isPresent()){
            return taskOptional.get();
        }else{
            throw new ResourceNotFoundException("Task not found.");
        } 
    }

    //READ

    @PreAuthorize("isAuthenticated()")
    public List<Task> findAll(){
        return taskRepo.findAll();
    }

    @PreAuthorize("@securityService.hasAccessToTask(#id, authentication)")
    public Task getById(Integer id){
        Task taskFound = checkedExistsById(id);
        return taskFound;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Task> securityGetAllTasks(Project project, CustomUserDetails customUserDetails){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        List<Task> tasks = taskRepo.findByProject(project);
        return tasks;
    }

    @PreAuthorize("@securityService.hasAccessToTask(#id, authentication)")  //run method hasAccessToProject passing params id & customUserDetails logged, if returns true run the method below
    public Task securityGetSingleTask(Integer id, Project project, CustomUserDetails customUserDetails){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        Task task = findByIdAndProject(id, project);
        return task;
    }


    //CREATE
    @PreAuthorize("isAuthenticated()")
    public Task create(Task taskToCreate, CustomUserDetails customUserDetails){
        if(taskToCreate == null){
            throw new IllegalArgumentException("Task to create cannot be null.");
        }
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        projectRepo.save(taskToCreate.getProject());

        Task savedTask = taskRepo.save(taskToCreate);
        System.out.println("Saved task from DB: " + taskRepo.findById(savedTask.getId()).get()); //x debug GET DATA FROM THE DB!
        return savedTask;
    }

    //UPDATE
    @PreAuthorize("@securityService.hasAccessToTask(#taskToEdit.id, authentication)")
    public Task edit(Task taskToEdit, CustomUserDetails customUserDetails){
        if(taskToEdit == null){
            throw new IllegalArgumentException("Task to update cannot be null.");
        }
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        Task existingTask = checkedExistsById(taskToEdit.getId());
        
        existingTask.setTitle(taskToEdit.getTitle());
        existingTask.setDescription(taskToEdit.getDescription());
        existingTask.setStatus(taskToEdit.getStatus());
        existingTask.setIsActive(taskToEdit.getIsActive());
        existingTask.setIsCompleted(taskToEdit.getIsCompleted());
        existingTask.setPriority(taskToEdit.getPriority());
        existingTask.setDueDate(taskToEdit.getDueDate());
        existingTask.setTaskStartDate(taskToEdit.getTaskStartDate());
        existingTask.setTaskEndDate(taskToEdit.getTaskEndDate());

        existingTask.setProject(taskToEdit.getProject());

        Task updatedTask = taskRepo.save(existingTask);
        System.out.println("Updated task from DB: " + taskRepo.findById(updatedTask.getId()).get());
        return updatedTask;
    }

    //DELETE
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToTask(#taskToDelete.id, authentication)")
    public void delete(Task taskToDelete, CustomUserDetails customUserDetails){
        if(taskToDelete == null){
            throw new IllegalArgumentException("Task to delete cannot be null.");
        }
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        taskRepo.delete(taskToDelete);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToTask(#id, authentication)")
    public void deleteById(Integer id, CustomUserDetails customUserDetails){
        Task taskToDelete = checkedExistsById(id);
        Task task = securityGetSingleTask(id, taskToDelete.getProject(), customUserDetails);
        taskRepo.delete(task);
    }


    //FILTERS

    public List<Task> findByProject(Project project){
        return taskRepo.findByProject(project);
    }
    
    public Task findByIdAndProject(Integer taskId, Project project){
        Task task = taskRepo.findByIdAndProject(taskId, project);
        if(task == null) {
            throw new AccessDeniedException("You do not have access to this task.");
        }
        return task;
    }
}
