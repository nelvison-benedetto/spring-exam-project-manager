package org.lessons.exam.spring_examprojectmanager.services;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.ProjectRepo;
import org.lessons.exam.spring_examprojectmanager.repository.TaskRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("securityService")  //securityService x call it easily in the @PreAuthorize
public class SecurityService {
    
    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final TaskRepo taskRepo;

    @Autowired
    public SecurityService(ProjectRepo projectRepo, UserService userService, TaskRepo taskRepo){
        this.projectRepo = projectRepo;
        this.userService = userService;
        this.taskRepo = taskRepo;
    }

    public Person checkPersonForActualUser(CustomUserDetails customUserDetails){
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            throw new RuntimeException("Person for this Security not found.");
        }
        return person;
    }

    //x Projects
    public Boolean hasAccessToProject(Integer projectId, Authentication authentication) {  //correct is org.springframework.security.core.Authentication;
        if(authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if(!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if(person == null){
            return false;
        }
        Project project = projectRepo.findById(projectId).orElse(null);
        return project != null && project.getPersons().contains(person);
    }

    //x Tasks
    public Boolean hasAccessToTask(Integer taskId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            return false;
        }
        Person person = userService.checkedExistsById(customUserDetails.getId()).getPerson();
        if (person == null) {
            return false;
        }
        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getProject() == null) {
            return false;
        }
        Project project = task.getProject();
        return project.getPersons().contains(person);
    }

}
