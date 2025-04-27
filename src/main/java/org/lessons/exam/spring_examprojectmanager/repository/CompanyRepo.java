package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Integer>{
    
    public boolean existsByCompanyEIN(String companyEIN);  //specific for my own field, i need to write it.

    public List<Company> findByPersonsContaining(Person person);
    public Company findByIdAndPersonsContaining(Integer companyId, Person person);
    
}
