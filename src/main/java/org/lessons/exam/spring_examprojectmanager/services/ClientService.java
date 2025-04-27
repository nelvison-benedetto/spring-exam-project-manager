package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.exceptions.DuplicateResourceException;
import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.ClientRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    
    private final ClientRepo clientRepo;
    private final CompanyService companyService;
    private final PersonService personService;
    private final SecurityService securityService;

    @Autowired
    public ClientService(ClientRepo clientRepo, @Lazy CompanyService companyService, PersonService personService, SecurityService securityService){  //USO LAZY OTHERWISE i services client & company call each other without starting
        this.clientRepo = clientRepo;
        this.companyService = companyService;
        this.personService = personService;
        this.securityService = securityService;
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
    @PreAuthorize("isAuthenticated()")
    public List<Client> findAll(){
        return clientRepo.findAll();
    }

    @PreAuthorize("@securityService.hasAccessToClient(#id, authentication)")
    public Client getById(Integer id){
        Client clientFound = checkedExistsById(id);
        return clientFound;
    }

    // @PreAuthorize("isAuthenticated()")
    // public List<Project> securityGetAllProjects(CustomUserDetails customUserDetails){
    //     Person person = checkPersonForActualUser(customUserDetails);
    //     List<Project> projects;
    //     if(person.getCompany() != null) {
    //         return projects = findByCompaniesContaining(person.getCompany());
    //     } else {  //TO FINISH, A PERSON LOGGED IN CAN ALSO SEE THE PROJECTS X HIS COMPANY
    //         return projects = findByPersonsContaining(person);
    //     }
    // }

    @PreAuthorize("@securityService.hasAccessToClient(#id, authentication)")  //run method hasAccessToClient passing params id & customUserDetails logged, if returns true run the method below
    public Client securityGetSingleClient(Integer id, CustomUserDetails customUserDetails){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        Client client = findByIdAndPerson(id, person);
        return client;
    }



    //CREATE
    //@PreAuthorize("isAuthenticated()")   //at the creation i haven't logged in yet
    public Client create(Client clientToCreate){
        if(clientToCreate == null){
            throw new IllegalArgumentException("Client to create cannot be null.");
        }
        //Person personSec = securityService.checkPersonForActualUser(customUserDetails);
        if(clientToCreate.getPerson() != null && clientToCreate.getPerson().getId() != null) {
            Person fullPerson = personService.checkedExistsById(clientToCreate.getPerson().getId());
            clientToCreate.setPerson(fullPerson);
            fullPerson.setClient(clientToCreate);
        }

        if(clientToCreate.getCompany() != null && clientToCreate.getCompany().getId() != null){
            Company fullCompany = companyService.checkedExistsById(clientToCreate.getCompany().getId());
            clientToCreate.setCompany(fullCompany);

            List<Person> validPersons = fullCompany.getPersons().stream()  //questo va bene per only x sign-up form, perche avrai legato alla company appena creata solo 1 person e 1 client
            .map(person -> personService.checkedExistsById(person.getId()))
            .collect(Collectors.toList());  //.collect() put the results in a new collection, //.toSet() to specifi collection Set<>
            
            for(Person person : validPersons) {
                person.setClient(clientToCreate);  //add this company to the obj person
            }
            
        }

        System.out.println("CreatedClient...: " + clientToCreate);
        Client savedClient = clientRepo.save(clientToCreate);
        System.out.println("Saved client from DB: " + clientRepo.findById(savedClient.getId()).get()); //x debug GET DATA FROM THE DB!
        return savedClient;

    }

    //UPDATE
    @PreAuthorize("@securityService.hasAccessToClient(#clientToEdit.id, authentication)")
    public Client edit(Client clientToEdit, CustomUserDetails customUserDetails){
        if(clientToEdit == null){
            throw new IllegalArgumentException("Client to update cannot be null.");
        }
        Client existingClient = securityGetSingleClient(clientToEdit.getId(), customUserDetails);
        
        existingClient.setSubscriptionType(clientToEdit.getSubscriptionType());
        existingClient.setStatus(clientToEdit.getStatus());
        existingClient.setSubscriptionStartDate(clientToEdit.getSubscriptionStartDate());
        existingClient.setSubscriptionEndDate(clientToEdit.getSubscriptionEndDate());

        //!old, but good settings
        //fresh upload from db to avoid errors with incomplete objects
        // List<Company> freshCompanies = existingClient.getCompanies().stream()  
        // .map(c -> companyService.checkedExistsById(c.getId()))
        // .toList();
        // //reset all & overwrite!!
        // existingClient.getCompanies().clear();  //rimuovi tutte le associazioni attuali
        // existingClient.getCompanies().addAll(freshCompanies);  //aggiungi quelle nuove

        
        return clientRepo.save(existingClient);
    }

    //DELETE
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToClient(#clientToDelete.id, authentication)")
    public void delete(Client clientToDelete, CustomUserDetails customUserDetails){
        if(clientToDelete == null){
            throw new IllegalArgumentException("Client to delete cannot be null.");
        }
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        clientRepo.delete(clientToDelete);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToClient(#id, authentication)")
    public void deleteById(Integer id, CustomUserDetails customUserDetails){
        Client clientToDelete = securityGetSingleClient(id, customUserDetails);
        clientRepo.delete(clientToDelete);
    }



    //FILTERS

    public Client findByPerson(Person person){
        return clientRepo.findByPerson(person);
    }

    public Client findByIdAndPerson(Integer clientId, Person person){
        return clientRepo.findByIdAndPerson(clientId, person);
    }


}
