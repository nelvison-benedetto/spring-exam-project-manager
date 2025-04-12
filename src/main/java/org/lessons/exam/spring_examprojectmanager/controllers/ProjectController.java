package org.lessons.exam.spring_examprojectmanager.controllers;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.Transient;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //READ
    @GetMapping
    public String projectsIndex(Model model){
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "entities/projects/index.html";
    }

    @GetMapping("/{id}")
    public String projectsShow(@PathVariable Integer id, Model model){
        Project project = projectService.getById(id);
        System.out.println(project);
        System.out.println(project.getTasks());

        model.addAttribute("project", project);
        return "entities/projects/show.html";
    }


    //CREATE
    @GetMapping("/create")
    public String projectsCreate(Model model){
        model.addAttribute("project", new Project());
        return "entities/projects/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String projectsStore(@Valid @ModelAttribute("project") Project project,
    BindingResult bindingResult, Model model){
        System.out.println(project);
        
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/projects/create-or-edit.html";
        }
        projectService.create(project);
        return "redirect:/projects";
    }

    
    //UPDATE
    @GetMapping("/{id}/edit")
    public String projectsUpdate(@PathVariable Integer id, Model model){
        model.addAttribute("project", projectService.getById(id));
        model.addAttribute("edit", true);
        return "entities/projects/create-or-edit.html";
    }
    @PostMapping("/{id}/update")
    public String projectsUpdate(@Valid @ModelAttribute("project") Project project,
    BindingResult bindingResult, @PathVariable Integer id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/projects/create-or-edit.html";
        }
        projectService.edit(project);
        return "redirect:/projects/" + id;
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String projectsDelete(@PathVariable Integer id){
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    //FILTERS
}
