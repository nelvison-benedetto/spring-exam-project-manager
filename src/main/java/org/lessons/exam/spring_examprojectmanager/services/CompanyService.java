package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class CompanyService {
    
    private final CompanyRepo companyRepo;
    @Autowired  //ora eseguito da Lombok w @RequiredArgsConstructor
    public CompanyService(CompanyRepo companyRepo){
        this.companyRepo = companyRepo;
    }
    

    public Boolean boolExistsById(Integer id){
        return companyRepo.existsById(id);
    }
    public Boolean boolExists(Company companyAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Company> companyOptional = companyRepo.findById(companyAskIfExists.getId());
        if(companyOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Company> optionalFindById(Integer id){
        return companyRepo.findById(id);
    }

    public Company checkedExistsById(Integer id){
        Optional<Company> companyOptional = companyRepo.findById(id);
        if(companyOptional.isPresent()){
            return companyOptional.get();
        }else{
            throw new ResourceNotFoundException("Company not found.");
        } 
    }

    //READ
    public List<Company> findAll(){
        return companyRepo.findAll();
    }

    public Company getById(Integer id){
        Company companyFound = checkedExistsById(id);
        return companyFound;
    }

    
    //CREATE
    public Company create(Company companyToCreate){
        if(companyToCreate == null){
            throw new IllegalArgumentException("Company to create cannot be null.");
        }
        if(companyRepo.existsByCompanyEIN(companyToCreate.getCompanyEIN())){
            throw new DuplicateResourceException("Company already exists for create.");
        }
        return companyRepo.save(companyToCreate);
    }

    //UPDATE
    public Company edit(Company companyToEdit){
        if(companyToEdit == null){
            throw new IllegalArgumentException("Company to update cannot be null.");
        }
        Company existingCompany = checkedExistsById(companyToEdit.getId());
        existingCompany.setCompanyLegalName(companyToEdit.getCompanyLegalName());
        existingCompany.setCompanyUsername(companyToEdit.getCompanyUsername());
        existingCompany.setCompanyEIN(companyToEdit.getCompanyEIN());
        existingCompany.setCompanyStateTaxID(companyToEdit.getCompanyStateTaxID());

        return companyRepo.save(existingCompany);
    }

    //DELETE
    public void delete(Company companyToDelete){
        if(companyToDelete == null){
            throw new IllegalArgumentException("Company to delete cannot be null.");
        }
        companyRepo.delete(companyToDelete);
    }
    public void deleteById(Integer id){
        Company companyToDelete = checkedExistsById(id);
        companyRepo.delete(companyToDelete);
    }
}
