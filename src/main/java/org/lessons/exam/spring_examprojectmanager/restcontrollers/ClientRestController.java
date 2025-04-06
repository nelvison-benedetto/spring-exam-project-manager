package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    
    private final ClientService clientService;
    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    //READ
    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Client> show(@PathVariable Integer id) {
        Optional<Client> clientOptional = clientService.optionalFindById(id);
        if (clientOptional.isPresent()) {
            return new ResponseEntity<Client>(clientOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
    }

    //CREATE
    @PostMapping
    public ResponseEntity<Client> store(@Valid @RequestBody Client client) {
        return new ResponseEntity<Client>(clientService.create(client), HttpStatus.CREATED);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@Valid @RequestBody Client client, @PathVariable Integer id) {
        if(clientService.optionalFindById(id).isPresent()) {
            client.setId(id);
            return new ResponseEntity<Client>(clientService.edit(client), HttpStatus.OK);
        }
        return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Integer id) {
        if(clientService.optionalFindById(id).isPresent()) {
            clientService.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
}
