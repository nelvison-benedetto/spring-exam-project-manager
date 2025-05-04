package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Integer>{

    public List<Person> findByEmailContainingAndPhoneNumberContainingAndIdNot(String email, String phoneNumber, Integer personId);
    public List<Person> findByEmailContainingAndIdNot(String email, Integer personId);
    public List<Person> findByPhoneNumberContainingAndIdNot(String phoneNumber, Integer personId);
    public List<Person> findByIdNot(Integer personId);
    
}
