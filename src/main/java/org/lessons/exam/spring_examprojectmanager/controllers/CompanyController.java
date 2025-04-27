package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.ArrayList;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
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
@RequestMapping("/companies")
public class CompanyController {
    
    private final CompanyService companyService;
    private final PersonService personService;

    @Autowired
    public CompanyController(CompanyService companyService, PersonService personService) {
        this.companyService = companyService;
        this.personService = personService;
    }   

    //READ
    @GetMapping
    public String companiesIndex(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        List<Company> companies = companyService.securityGetAllCompanies(customUserDetails);
        model.addAttribute("companies", companies);
        return "entities/companies/index.html";
    }

    @GetMapping("/{id}")
    public String companiesShow(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Company company = companyService.securityGetSingleCompany(id, customUserDetails);
        if(company == null){
            return "errors/404.html";
        }
        model.addAttribute("company", company);
        return "entities/companies/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String companiesCreate(@RequestParam("personId") Integer personId, Model model){
        Company company = new Company();
        Person person = personService.checkedExistsById(personId);

        List<Person> personList = new ArrayList<>();
        personList.add(person);
        company.setPersons(personList);  //easy logic only x pass the person of personId to the render

        model.addAttribute("company", company);
        return "entities/companies/create-or-edit.html";
    }   

    @PostMapping("/store")
    public String companiesStore(@Valid @ModelAttribute("company") Company company,
    BindingResult bindingResult,  Model model){
        if(bindingResult.hasErrors()){
            return "entities/companies/create-or-edit.html";
        }
        System.out.println(company);
        Company savedCompany = companyService.create(company);
        System.out.println("after .create() method...");
        return "redirect:/clients/create?companyId=" + savedCompany.getId();
    }

    //UPDATE
    @GetMapping("/edit/{id}")
    public String companiesUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Company company = companyService.securityGetSingleCompany(id, customUserDetails);
        model.addAttribute("company", company);
        model.addAttribute("edit", true);
        return "entities/companies/create-or-edit.html";
    }
    @PostMapping("/update/{id}")
    public String companiesUpdate(@Valid @ModelAttribute("company") Company company, @PathVariable Integer id,
    BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/companies/create-or-edit.html";
        }
        companyService.edit(company, customUserDetails);
        return "redirect:/projects";  //TODO To redirect better
    }


    //DELETE
    @GetMapping("/delete/{id}")
    public String companiesDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        companyService.deleteById(id, customUserDetails);
        return "redirect:/projects";  //TODO To redirect better
    }


    
}
