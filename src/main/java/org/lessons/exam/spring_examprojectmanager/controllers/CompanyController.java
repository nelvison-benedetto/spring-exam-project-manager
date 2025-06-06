package org.lessons.exam.spring_examprojectmanager.controllers;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.lessons.exam.spring_examprojectmanager.services.SecurityService;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    
    private final CompanyService companyService;
    private final PersonService personService;
    private final SecurityService securityService;
    private final ProjectService projectService;

    @Autowired
    public CompanyController(CompanyService companyService, PersonService personService, SecurityService securityService, ProjectService projectService) {
        this.companyService = companyService;
        this.personService = personService;
        this.securityService = securityService;
        this.projectService = projectService;
    }   

    //READ
    @GetMapping
    public String companiesIndex(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        List<Company> companies = companyService.securityGetAllCompanies(customUserDetails);
        model.addAttribute("companies", companies);
        return "entities/companies/index.html";
    }

    @GetMapping("/{id}")
    public String companiesShow(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Authentication authentication, Model model){  //Authentication from 'security.core....' !
        //Company company = companyService.securityGetSingleCompany(id, customUserDetails);
        Company company = companyService.getById(id);  //using only @PreAuthorize("isAuthenticated()") to show it to anyone!!
        if(company == null){
            return "errors/404.html";
        }
        model.addAttribute("company", company);
        model.addAttribute("canEditCompany", securityService.hasAccessToCompany(company.getId(), authentication));
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
    public String companiesStore(@Valid @ModelAttribute("company") Company company,  //Valid x validate the fields (e.g. @NoBlank)
    BindingResult bindingResult,  Model model){
        if(bindingResult.hasErrors()){
            return "entities/companies/create-or-edit.html";
        }
        Company savedCompany = companyService.create(company);
        return "redirect:/clients/create?companyId=" + savedCompany.getId();
    }

    //UPDATE
    @GetMapping("/{id}/edit")
    public String companiesUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Company company = companyService.securityGetSingleCompany(id, customUserDetails);
        model.addAttribute("company", company);
        model.addAttribute("edit", true);
        return "entities/companies/create-or-edit.html";
    }
    @PutMapping("/{id}/update")
    public String companiesUpdate(@Valid @ModelAttribute("company") Company company,
    BindingResult bindingResult, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("edit", true);
            return "entities/companies/create-or-edit.html";
        }
        companyService.edit(company, customUserDetails);
        return "redirect:/companies/" + companyService.securityGetSingleCompany(id, customUserDetails).getId();
    }


    //DELETE
    @DeleteMapping("/{id}/delete")
    public String companiesDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        companyService.deleteById(id, customUserDetails);
        return "redirect:/persons/single-or-company?personId="+person.getId();
        //return "redirect:/";
    }

    //OTHERS

    @GetMapping("/{companyId}/associate/{personId}")
    public String associatePersonToCompany(@PathVariable Integer companyId, @PathVariable Integer personId){
        personService.personsAssociatePersonToCompany(companyId, personId);
        return "redirect:/companies/" + companyId;
    }

    @GetMapping("/partner-company")   //equivalent to '@GetMapping("/recruit-person")' of PersonController
    public String companiesPartnerCompany(
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){

        List<Company> companies = companyService.companiesFindAllLessMain(customUserDetails);
        model.addAttribute("companies", companies);

        if (projectId != null){
            Project project = projectService.getByIdNoSecMain(projectId);
            model.addAttribute("project", project);
        }
        return "entities/companies/partner-company.html";
    }

    @GetMapping("/searchByForm")
    public String companiesSearchByForm(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @RequestParam(name="companyLegalName", required = false, defaultValue = "") String companyLegalName,
        @RequestParam(name="companyUsername", required = false, defaultValue = "") String companyUsername,
        Model model
    ){
        List<Company> companies = companyService.companiesSearchByForm(companyLegalName, companyUsername, customUserDetails);
        if(projectId != null){
            Project project = projectService.getByIdNoSecMain(projectId);
            model.addAttribute("project", project);
        }else{System.out.println("NO PROJECTID FOUND!!!!");}
        model.addAttribute("companies", companies);
        return "entities/companies/partner-company.html";
    }

}
