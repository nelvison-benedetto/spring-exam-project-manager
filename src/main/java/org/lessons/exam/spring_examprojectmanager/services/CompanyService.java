package org.lessons.exam.spring_examprojectmanager.services;

import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    
    private final CompanyRepo companyRepo;
    @Autowired
    public CompanyService(CompanyRepo companyRepo){
        this.companyRepo = companyRepo;
    }

    
}
