package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    
    private final TaskRepo taskRepo;
    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
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
    public List<Task> findAll(){
        return taskRepo.findAll();
    }

    public Task getById(Integer id){
        Task taskFound = checkedExistsById(id);
        return taskFound;
    }

    //CREATE
    public Task create(Task taskToCreate){
        if(taskToCreate == null){
            throw new IllegalArgumentException("Task to create cannot be null.");
        }
        if(boolExists(taskToCreate)){
            throw new DuplicateResourceException("Task already exists for create.");
        }
        return taskRepo.save(taskToCreate);
    }

    //UPDATE
    public Task edit(Task taskToEdit){
        if(taskToEdit == null){
            throw new IllegalArgumentException("Task to update cannot be null.");
        }
        Task existingTask = checkedExistsById(taskToEdit.getId());
        
        existingTask.setTitle(taskToEdit.getTitle());
        existingTask.setDescription(taskToEdit.getDescription());
        existingTask.setStatus(taskToEdit.getStatus());
        existingTask.setActive(taskToEdit.isActive());
        existingTask.setCompleted(taskToEdit.isCompleted());
        existingTask.setPriority(taskToEdit.getPriority());
        existingTask.setDueDate(taskToEdit.getDueDate());
        existingTask.setTaskStartDate(taskToEdit.getTaskStartDate());
        existingTask.setTaskEndDate(taskToEdit.getTaskEndDate());

        return taskRepo.save(existingTask);
    }

    //DELETE
    public void delete(Task taskToDelete){
        if(taskToDelete == null){
            throw new IllegalArgumentException("Task to delete cannot be null.");
        }
        taskRepo.delete(taskToDelete);
    }

    public void deleteById(Integer id){
        Task taskToDelete = checkedExistsById(id);
        taskRepo.delete(taskToDelete);
    }
}
