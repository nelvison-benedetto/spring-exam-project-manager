package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.dto.ProjectDTO;
import org.lessons.exam.spring_examprojectmanager.dto.TaskDTO;
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
    public ResponseEntity<List<ProjectDTO>> projectsRestIndex() {
        List<Project> projects = projectRepo.findAll();
        List<ProjectDTO> projectDTOs = projects.stream().map(ProjectDTO::new).toList();
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> projectsRestShow(@PathVariable Integer id) {
        if(!projectRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Project> project = projectRepo.findById(id);
        ProjectDTO projectDTO = new ProjectDTO(project.get());
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }


}
