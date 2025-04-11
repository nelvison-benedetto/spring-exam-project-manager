package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    
    private final TaskService taskService;  
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //READ
    @GetMapping
    public String tasksIndex(Model model){
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "entities/tasks/index.html";
    }

    @GetMapping("/{id}")
    public String tasksShow(@PathVariable Integer id, Model model){
        Task task = taskService.getById(id);
        model.addAttribute("task", task);
        return "entities/tasks/show.html";
    }


    //CREATE
    @GetMapping("/create")
    public String tasksCreate(Model model){
        model.addAttribute("task", new Task());
        return "entities/tasks/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String tasksStore(@Valid @ModelAttribute("task") Task task,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/tasks/create-or-edit.html";
        }
        taskService.create(task);
        return "redirect:/tasks";
    }

    //UPDATE
    @GetMapping("/edit/{id}")
    public String tasksUpdate(@PathVariable Integer id, Model model){
        model.addAttribute("task", taskService.getById(id));
        model.addAttribute("edit", true);
        return "entities/tasks/create-or-edit.html";
    }
    @PostMapping("/update/{id}")
    public String tasksUpdate(@Valid @ModelAttribute("task") Task task, @PathVariable Integer id,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/tasks/create-or-edit.html";
        }
        taskService.edit(task);
        return "redirect:/tasks";
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String tasksDelete(@PathVariable Integer id){
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    //FILTERS

}
