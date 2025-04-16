package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    //READ
    @GetMapping
    public String clientsIndex(Model model){
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "entities/clients/index.html";
    }

    @GetMapping("/{id}")
    public String clientsShow(@PathVariable Integer id, Model model){
        Client client = clientService.getById(id);
        model.addAttribute("client", client);
        return "entities/clients/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String clientsCreate(Model model){
        model.addAttribute("client", new Client());
        return "entities/clients/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String clientsStore(@Valid @ModelAttribute("client") Client client,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/clients/create-or-edit.html";
        }
        clientService.create(client);
        return "redirect:/security/sign-in";
    }

    //UPDATE
    @GetMapping("/edit/{id}")
    public String clientsUpdate(@PathVariable Integer id, Model model){
        model.addAttribute("client", clientService.getById(id));
        model.addAttribute("edit", true);
        return "entities/clients/create-or-edit.html";
    }
    @PostMapping("/update/{id}")
    public String clientsUpdate(@Valid @ModelAttribute("client") Client client, @PathVariable Integer id,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/clients/create-or-edit.html";
        }
        clientService.edit(client);
        return "redirect:/clients";
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String clientsDelete(@PathVariable Integer id){
        clientService.deleteById(id);
        return "redirect:/clients";
    }

    //FILTERS

}
