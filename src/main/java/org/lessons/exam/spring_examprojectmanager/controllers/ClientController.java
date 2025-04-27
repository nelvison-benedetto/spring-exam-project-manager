package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.ClientService;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;
    private final PersonService personService;
    private final CompanyService companyService;

    @Autowired
    public ClientController(ClientService clientService, PersonService personService, CompanyService companyService) {
        this.clientService = clientService;
        this.personService = personService;
        this.companyService = companyService;
    }

    //READ
    // @GetMapping
    // public String clientsIndex(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
    //     List<Client> clients = clientService.findAll();
    //     model.addAttribute("clients", clients);
    //     return "entities/clients/index.html";
    // }

    @GetMapping("/{id}")
    public String clientsShow(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Client client = clientService.securityGetSingleClient(id, customUserDetails);
        if(client == null){
            return "errors/404.html";
        }
        model.addAttribute("client", client);
        return "entities/clients/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String clientsCreate(@RequestParam(value = "personId", required = false) Integer personId,
    @RequestParam(value = "companyId", required = false) Integer companyId,
     Model model){
        Client client = new Client();

        if(personId != null){
            Person person = personService.checkedExistsById(personId);   //TO DO SECURITY HERE 
            client.setPerson(person);
        }
        if(companyId != null){
            Company company = companyService.checkedExistsById(companyId);   //TO DO SECURITY HERE 
            client.setCompany(company);
        }

        model.addAttribute("client", client);
        return "entities/clients/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String clientsStore(@Valid @ModelAttribute("client") Client client,
    BindingResult bindingResult,  Model model){
        if(bindingResult.hasErrors()){
            return "entities/clients/create-or-edit.html";
        }
        Client savedClient = clientService.create(client);
        return "redirect:/security/sign-in";
    }

    //UPDATE
    @GetMapping("/edit/{id}")
    public String clientsUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Client client = clientService.securityGetSingleClient(id, customUserDetails);
        model.addAttribute("client", client);
        model.addAttribute("edit", true);
        return "entities/clients/create-or-edit.html";
    }
    
    @PostMapping("/update/{id}")
    public String clientsUpdate(@Valid @ModelAttribute("client") Client client, @PathVariable Integer id,
    BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/clients/create-or-edit.html";
        }
        clientService.edit(client, customUserDetails);
        return "redirect:/projects";  //TODO To redirect better
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String clientsDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        clientService.deleteById(id, customUserDetails);
        return "redirect:/projects";  //TODO To redirect better
    }



}
