package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {
    
    private final TaskRepo taskRepo;
    @Autowired
    public TaskRestController(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    //READ
    @GetMapping
    public ResponseEntity<List<Task>> tasksRestIndex() {
        List<Task> tasks = taskRepo.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> tasksRestShow(@PathVariable Integer id) {
        if(!taskRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Task> task = taskRepo.findById(id);
        return new ResponseEntity<>(task.get(), HttpStatus.OK);
    }
    
}
