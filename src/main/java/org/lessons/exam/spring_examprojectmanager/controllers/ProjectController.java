package org.lessons.exam.spring_examprojectmanager.controllers;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.lessons.exam.spring_examprojectmanager.services.UserService;
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

import jakarta.persistence.Transient;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    
    private final ProjectService projectService;
    private final UserService userService;
    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    //READ
    @GetMapping
    public String projectsIndex(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<Project> projects = projectService.securityGetAllProjects(customUserDetails);
        //List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "entities/projects/index.html";
    }

    @GetMapping("/{id}")
    public String projectsShow(@PathVariable Integer id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Project project = projectService.securityGetSingleProject(id, customUserDetails);
        if(project == null){
            return "errors/404.html";
        }
        //Project project = projectService.getById(id);
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
    BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/projects/create-or-edit.html";
        }
        projectService.create(project, customUserDetails);
        return "redirect:/projects";
    }

    
    //UPDATE
    @GetMapping("/{id}/edit")
    public String projectsUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        //model.addAttribute("project", projectService.getById(id));
        Project project = projectService.securityGetSingleProject(id, customUserDetails);
        model.addAttribute("project", project);
        model.addAttribute("edit", true);
        return "entities/projects/create-or-edit.html";
    }

    @PutMapping("/{id}/update")
    public String projectsUpdate(@Valid @ModelAttribute("project") Project project,
    BindingResult bindingResult, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/projects/create-or-edit.html";
        }
        projectService.edit(project, customUserDetails);
        return "redirect:/projects/" + projectService.securityGetSingleProject(id, customUserDetails).getId();
    }

    //DELETE
    @DeleteMapping("/{id}/delete")
    public String projectsDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        projectService.deleteById(id, customUserDetails);
        return "redirect:/projects";
    }

    
}
