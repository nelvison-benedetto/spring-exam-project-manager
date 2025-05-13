package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.lessons.exam.spring_examprojectmanager.services.SecurityService;
import org.lessons.exam.spring_examprojectmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/persons")
public class PersonController {
    
    private final PersonService personService;
    private final UserService userService;
    private final SecurityService securityService;
    private final ProjectService projectService;
    private final CompanyService companyService;

    @Autowired
    public PersonController(PersonService personService, UserService userService, SecurityService securityService, ProjectService projectService, CompanyService companyService) {
        this.personService = personService;
        this.userService = userService;
        this.securityService = securityService;
        this.projectService = projectService;
        this.companyService = companyService;
    }

    //READ
    @GetMapping
    public String personsIndex(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        List<Person> persons = personService.findAll();
        model.addAttribute("persons", persons);
        return "entities/persons/index.html";
    }

    @GetMapping("/{id}")
    public String personsShow(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Authentication authentication, Model model){  //Authentication from 'security.core....' !
        Person person = personService.getById(id);
        model.addAttribute("person", person);
        model.addAttribute("canEditPerson", securityService.hasAccessToPerson(person.getId(), authentication));
        return "entities/persons/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String personsCreate(@RequestParam("userId") Integer userId, Model model){
        Person person = new Person();
        User user = userService.checkedExistsById(userId);
        person.setUser(user);
        model.addAttribute("person", person);

        return "entities/persons/create-or-edit.html";
    }

    @PostMapping("/store")
    public String personsStore(@Valid @ModelAttribute("person") Person person,
    BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "entities/persons/create-or-edit.html";
        }
        Person savedPerson =personService.create(person);
        return "redirect:/persons/single-or-company?personId=" + savedPerson.getId();
    }

    //UPDATE
    @GetMapping("/{id}/edit")
    public String personsEdit(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        Person person = personService.getById(id);
        model.addAttribute("person", person);
        model.addAttribute("edit", true);
        return "entities/persons/create-or-edit.html";
    }

    @PutMapping("/{id}/update")
    public String personsUpdate(@Valid @ModelAttribute("person") Person person,
    BindingResult bindingResult, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "entities/persons/create-or-edit.html";
        }
        personService.edit(person, customUserDetails);
        return "redirect:/persons/" + personService.securityGetSinglePerson(id, customUserDetails).getId();
    }

    //DELETE

    //this never used, if u want to delete ur account delete the User
    @DeleteMapping("/{id}/delete")
    public String personsDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        personService.deleteById(id);
        return "redirect:/";
    }


    //OTHERS

    @GetMapping("/single-or-company")
    public String personsChoosePersonOrCompany(@RequestParam("personId") Integer personId, Model model) {
        model.addAttribute("personId", personId);
        return "entities/persons/single-or-company.html";
    }

    @GetMapping("/recruit-person")  //REMINDER se riceve projectId allora link project-person, se no link company-person (questo lo setti se arrivi da page persons/{id}). 
    public String personsRecruitPerson(
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @RequestParam(value = "companyId", required = false) Integer companyId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model ) {

        if(companyId != null){
            List<Person> persons = personService.personsFindAllLessMainLessWithoutCompany(customUserDetails); //I obtain only the persons that are not yet linked to any company!!
            model.addAttribute("persons", persons);
            Company company = companyService.getByIdNoSecMain(companyId);
            model.addAttribute("company", company);      
        }else{
            List<Person> persons = personService.personsFindAllLessMain(customUserDetails);
            model.addAttribute("persons", persons);
        }

        if(projectId != null){
            Project project = projectService.getByIdNoSecMain(projectId);
            model.addAttribute("project", project);
        }

        return "entities/persons/recruit-person.html";
    }

    @GetMapping("/searchByForm")
    public String personsSearchByForm(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @RequestParam(value = "companyId", required = false) Integer companyId,
        @RequestParam(name="email", required = false, defaultValue = "") String email,
        @RequestParam(name="phoneNumber", required = false, defaultValue = "") String phoneNumber,
        Model model
    ){
        List<Person> persons = personService.personsSearchByForm(email, phoneNumber, customUserDetails);
        if(projectId != null){
            Project project = projectService.getByIdNoSecMain(projectId);
            model.addAttribute("project", project);
        }
        if(companyId != null){
            Company company = companyService.getByIdNoSecMain(companyId);
            model.addAttribute("company", company);
        }
        model.addAttribute("persons", persons);
        return "entities/persons/recruit-person.html";
    }

}
