package org.lessons.exam.spring_examprojectmanager.repository;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Integer>{
    
}
