package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
}
