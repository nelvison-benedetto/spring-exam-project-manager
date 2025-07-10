package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.dto.ClientDTO;
import org.lessons.exam.spring_examprojectmanager.dto.TaskDTO;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.repository.ClientRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.ClientService;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.datafaker.providers.base.Company;

@RestController  //= @Controller + @ResponseBody(serializza in json format)
@RequestMapping("/api/clients")
public class ClientRestController {

    private final ClientService clientService;
    private final PersonService personService;
    private final CompanyService companyService;
    private final ClientRepo clientRepo;

    @Autowired
    public ClientRestController(ClientService clientService, PersonService personService, CompanyService companyService, ClientRepo clientRepo) {
        this.clientService = clientService;
        this.personService = personService;
        this.companyService = companyService;
        this.clientRepo = clientRepo;
    }

    //READ
    @GetMapping
    public ResponseEntity<List<ClientDTO>> clientsRestIndex(){  //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        List<Client> clients = clientRepo.findAll();
        List<ClientDTO> clientDTOs = clients.stream().map(ClientDTO::new).toList(); //.map() esegue new ClientDTO(client)
        return ResponseEntity.ok(clientDTOs);
    }

    @GetMapping("/id")
    public ResponseEntity<ClientDTO> clientsRestShow(@PathVariable Integer id){   //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        if(!clientService.boolExistsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Client> client = clientRepo.findById(id);
        ClientDTO clientDTO = new ClientDTO(client.get());
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }


    
    // //CREATE
    // @PostMapping
    // public ResponseEntity<Client> clientsRestStore(@Valid @RequestBody Client clientToStore){
    //     Client clientStored = clientService.create(clientToStore);
    //     return new ResponseEntity<>(clientStored, HttpStatus.CREATED);
    // }

    // //UPDATE
    // @PutMapping("/{id}")
    // public ResponseEntity<Client> clientsRestUpdate(@Valid @RequestBody Client clientToUpdate, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!clientService.boolExistsById(id)){
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     clientToUpdate.setId(id);
    //     Client clientUpdated = clientService.edit(clientToUpdate, customUserDetails);
    //     return new ResponseEntity<>(clientUpdated, HttpStatus.OK);
    // }

    // //DELETE
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> clientsRestDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!clientService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     clientService.deleteById(id, customUserDetails);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

}
