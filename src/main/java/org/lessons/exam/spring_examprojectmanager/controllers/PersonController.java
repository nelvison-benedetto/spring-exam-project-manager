package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/persons")
public class PersonController {
    
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    //READ
    @GetMapping
    public String personsIndex(Model model){
        List<Person> persons = personService.findAll();
        model.addAttribute("persons", persons);
        return "entities/persons/index.html";
    }

    @GetMapping("/{id}")
    public String personsShow(@PathVariable Integer id, Model model){
        Person person = personService.getById(id);
        model.addAttribute("person", person);
        return "entities/persons/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String personsCreate(Model model){
        model.addAttribute("person", new Person());
        return "entities/persons/create-or-edit.html";
    }

    @PostMapping("/store")
    public String personsStore(@Valid @ModelAttribute("person") Person person,
    BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "entities/persons/create-or-edit.html";
        }
        System.out.println(person);
        personService.create(person);
        return "redirect:/persons/single-or-company";
    }

    @GetMapping("/single-or-company")
    public String continueAsPerson() {
        return "entities/persons/single-or-company.html";
    }

    //UPDATE
    @GetMapping("/{id}/edit")
    public String personsEdit(@PathVariable Integer id, Model model) {
        Person person = personService.getById(id);
        model.addAttribute("person", person);
        model.addAttribute("edit", true);
        return "entities/persons/create-or-edit.html";
    }

    @PutMapping("/{id}/update")
    public String personsUpdate(@Valid @ModelAttribute("person") Person person,
    BindingResult bindingResult, @PathVariable Integer id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "entities/persons/create-or-edit.html";
        }
        personService.edit(person);
        return "redirect:/persons/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}/delete")
    public String personsDelete(@PathVariable Integer id) {
        personService.deleteById(id);
        return "redirect:/persons";
    }

    //FILTERS
}
