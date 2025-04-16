package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    
    private final PersonRepo personRepo;
    @Autowired 
    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }


    public Boolean boolExistsById(Integer id){
        return personRepo.existsById(id);
    }

    public Boolean boolExists(Person personAskIfExists){
        Optional<Person> personOptional = personRepo.findById(personAskIfExists.getId());
        if(personOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Person> optionalFindById(Integer id){
        return personRepo.findById(id);
    }

    public Person checkedExistsById(Integer id){
        Optional<Person> personOptional = personRepo.findById(id);
        if(personOptional.isPresent()){
            return personOptional.get();
        }else{
            throw new ResourceNotFoundException("Person not found.");
        }
    }

    
    //READ
    public List<Person> findAll(){
        return personRepo.findAll();
    }

    public Person getById(Integer id){
        Person personFound = checkedExistsById(id);
        return personFound;
    }

    
    //CREATE
    public Person create(Person personToCreate){
        if(personToCreate == null){
            throw new IllegalArgumentException("Person to create cannot be null.");
        }
        return personRepo.save(personToCreate);
    }

    //UPDATE
    public Person edit(Person personToEdit){
        if(personToEdit == null){
            throw new IllegalArgumentException("Person to update cannot be null.");
        }
        Person existingPerson = checkedExistsById(personToEdit.getId());

        //update one field at a time!(x security & x help Spring)
        existingPerson.setFirstname(personToEdit.getFirstname());
        existingPerson.setLastname(personToEdit.getLastname());
        //existingPerson.setUsername(personToEdit.getUsername());
        existingPerson.setEmail(personToEdit.getEmail());
        existingPerson.setPhoneNumber(personToEdit.getPhoneNumber());
        existingPerson.setCountry(personToEdit.getCountry());
        existingPerson.setBirthdate(personToEdit.getBirthdate());

        return personRepo.save(existingPerson);
    }

    
    //DELETE
    public void delete(Person personToDelete){
        if(personToDelete == null){
            throw new IllegalArgumentException("Person to delete cannot be null.");
        }
        personRepo.delete(personToDelete);
    }

    public void deleteById(Integer id){
        Person personToDelete = checkedExistsById(id);
        personRepo.delete(personToDelete);
    }

}
