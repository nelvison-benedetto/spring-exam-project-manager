package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {
    
    private final ProjectRepo projectRepo;
    
    @Autowired
    public ProjectRestController(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    //READ
    @GetMapping
    public ResponseEntity<List<Project>> projectsRestIndex() {
        List<Project> projects = projectRepo.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> projectsRestShow(@PathVariable Integer id) {
        if(!projectRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Project> project = projectRepo.findById(id);
        return new ResponseEntity<>(project.get(), HttpStatus.OK);
    }


}
