package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Integer>{
    
    public List<Project> findByPersonsContaining(Person person);
    public List<Project> findByCompaniesContaining(Company company);
    public Project findByIdAndPersonsContaining(Integer projectId, Person person);
    public Project findByIdAndCompaniesContaining(Integer projectId, Company company);
}
