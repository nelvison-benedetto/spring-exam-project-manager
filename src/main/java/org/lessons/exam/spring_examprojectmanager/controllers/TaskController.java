package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.lessons.exam.spring_examprojectmanager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    
    private final TaskService taskService;  
    private final ProjectService projectService;
    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    //READ
    @GetMapping
    public String tasksIndex(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        //maybe here get project using @RequestParam ( value="projectId", required = false) Integer projectId
        List<Task> tasks = taskService.securityGetAllTasks(null, customUserDetails);
        model.addAttribute("tasks", tasks);
        return "entities/tasks/index.html";
    }

    @GetMapping("/{id}")
    public String tasksShow(@PathVariable Integer id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Task noSecurityTask = taskService.getById(id);
        Task securityTask = taskService.securityGetSingleTask(id, noSecurityTask.getProject(), customUserDetails);
        model.addAttribute("task", securityTask);
        return "entities/tasks/show.html";
    }


    //CREATE
    @GetMapping("/{id}/create")
    public String tasksCreate(@PathVariable Integer id , @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Task task = new Task();
        task.setProject(projectService.securityGetSingleProject(id, customUserDetails));
        model.addAttribute("task", task);
        return "entities/tasks/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String tasksStore(@Valid @ModelAttribute("task") Task task,
    BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        System.out.println(task);
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/tasks/create-or-edit.html";
        }
        taskService.create(task, customUserDetails);
        return "redirect:/projects/"+ task.getProject().getId();
    }

    //UPDATE
    @GetMapping("/{id}/edit")
    public String tasksUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Task noSecurityTask = taskService.getById(id);
        Task securityTask = taskService.securityGetSingleTask(id, noSecurityTask.getProject(), customUserDetails);
        model.addAttribute("task", securityTask );
        model.addAttribute("edit", true);
        return "entities/tasks/create-or-edit.html";
    }
    @PutMapping("/{id}/update")
    public String tasksUpdate(@Valid @ModelAttribute("task") Task task,
    BindingResult bindingResult, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/tasks/create-or-edit.html";
        }
        Task updatedTask = taskService.edit(task, customUserDetails);
        return "redirect:/projects/" + updatedTask.getProject().getId();
    }

    //DELETE
    @DeleteMapping("/{id}/delete")
    public String tasksDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Integer projectId = taskService.getById(id).getProject().getId();
        taskService.deleteById(id, customUserDetails);
        return "redirect:/projects/" + projectId;
    }


    
}
