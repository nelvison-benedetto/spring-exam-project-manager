package org.lessons.exam.spring_examprojectmanager.controllers;

import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    
    public final ProjectRepo projectRepo;
    @Autowired
    public ProjectController(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    //READ
    

}
