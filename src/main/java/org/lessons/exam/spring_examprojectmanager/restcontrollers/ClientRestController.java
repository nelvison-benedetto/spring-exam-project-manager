package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import org.lessons.exam.spring_examprojectmanager.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    
    private final ClientService clientService;
    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    

}
