package org.lessons.exam.spring_examprojectmanager.repository;


import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{
    
}
