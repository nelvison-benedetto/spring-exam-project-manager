package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
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
@RequestMapping("/company")
public class CompanyController {
    
    private final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }   

    //READ
    @GetMapping
    public String companiesIndex(Model model){
        List<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "entities/companies/index.html";
    }

    @GetMapping("/{id}")
    public String companiesShow(@PathVariable Integer id, Model model){
        Company company = companyService.getById(id);
        model.addAttribute("company", company);
        return "entities/companies/show.html";
    }

    //CREATE
    @GetMapping("/create")
    public String companiesCreate(Model model){
        model.addAttribute("company", new Company());
        return "entities/companies/create-or-edit.html";
    }   
    @PostMapping("/store")
    public String companiesStore(@Valid @ModelAttribute("company") Company company,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            //add even the lists
            return "entities/companies/create-or-edit.html";
        }
        companyService.create(company);
        return "redirect:/companies";
    }

    //UPDATE
    @GetMapping("/edit/{id}")
    public String companiesUpdate(@PathVariable Integer id, Model model){
        model.addAttribute("company", companyService.getById(id));
        model.addAttribute("edit", true);
        return "entities/companies/create-or-edit.html";
    }
    @PostMapping("/update/{id}")
    public String companiesUpdate(@Valid @ModelAttribute("company") Company company, @PathVariable Integer id,
    BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/companies/create-or-edit.html";
        }
        companyService.edit(company);
        return "redirect:/companies";
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String companiesDelete(@PathVariable Integer id){
        companyService.deleteById(id);
        return "redirect:/companies";
    }

    //FILTERS

}
