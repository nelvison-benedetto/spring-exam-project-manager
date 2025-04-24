package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer>{
    
    public List<Task> findByProject(Project project);
    public Task findByIdAndProject(Integer taskId, Project project);

    
}
