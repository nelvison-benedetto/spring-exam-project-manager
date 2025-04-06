package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    
    private final ClientRepo clientRepo;
    private final CompanyService companyService;
    @Autowired
    public ClientService(ClientRepo clientRepo, @Lazy CompanyService companyService){  //USO LAZY OTHERWISE i services client & company call each other without starting
        this.clientRepo = clientRepo;
        this.companyService = companyService;
    }


    public Boolean boolExistsById(Integer id){
        return clientRepo.existsById(id);
    }
    public Boolean boolExists(Client clientAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Client> clientOptional = clientRepo.findById(clientAskIfExists.getId());
        if(clientOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Client> optionalFindById(Integer id){
        return clientRepo.findById(id);
    }

    public Client checkedExistsById(Integer id){
        Optional<Client> clientOptional = clientRepo.findById(id);
        if(clientOptional.isPresent()){
            return clientOptional.get();
        }else{
            throw new ResourceNotFoundException("Client not found.");
        } 
    }


    //READ
    public List<Client> findAll(){
        return clientRepo.findAll();
    }

    public Client getById(Integer id){
        Client clientFound = checkedExistsById(id);
        return clientFound;
    }

    //CREATE
    public Client create(Client clientToCreate){
        if(clientToCreate == null){
            throw new IllegalArgumentException("Client to create cannot be null.");
        }
        if(boolExists(clientToCreate)){
            throw new DuplicateResourceException("Client already exists for create.");
        }
        return clientRepo.save(clientToCreate);
    }

    //UPDATE
    public Client edit(Client clientToEdit){
        if(clientToEdit == null){
            throw new IllegalArgumentException("Client to update cannot be null.");
        }
        Client existingClient = checkedExistsById(clientToEdit.getId());
        
        existingClient.setSubscriptionType(clientToEdit.getSubscriptionType());
        existingClient.setStatus(clientToEdit.getStatus());
        existingClient.setSubscriptionStartDate(clientToEdit.getSubscriptionStartDate());
        existingClient.setSubscriptionEndDate(clientToEdit.getSubscriptionEndDate());


        //fresh upload from db to avoid errors with incomplete objects
        List<Company> freshCompanies = clientToEdit.getCompanies().stream()  
        .map(c -> companyService.checkedExistsById(c.getId()))
        .toList();
        //reset all & overwrite!!
        existingClient.getCompanies().clear();  //rimuovi tutte le associazioni attuali
        existingClient.getCompanies().addAll(freshCompanies);  //aggiungi quelle nuove

        
        return clientRepo.save(existingClient);
    }

    //DELETE
    public void delete(Client clientToDelete){
        if(clientToDelete == null){
            throw new IllegalArgumentException("Client to delete cannot be null.");
        }
        clientRepo.delete(clientToDelete);
    }

    public void deleteById(Integer id){
        Client clientToDelete = checkedExistsById(id);
        clientRepo.delete(clientToDelete);
    }
}
