package org.lessons.exam.spring_examprojectmanager.repository;

import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Integer>{
    
}
