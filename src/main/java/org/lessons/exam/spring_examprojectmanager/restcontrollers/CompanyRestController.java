package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("/api/companies")
public class CompanyRestController {
    
    private final CompanyService companyService;
    @Autowired
    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    //READ
    @GetMapping
    public List<Company> showAll(){
        List<Company> companies = companyService.findAll();
        return companies;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> show(@PathVariable Integer id){
        Optional<Company> companyOptional = companyService.optionalFindById(id);
        if(companyOptional.isPresent()){
            return new ResponseEntity<Company>(companyOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
        }
    }

    //CREATE
    @PostMapping
    public ResponseEntity<Company> store(@Valid @RequestBody Company company){  //i don't receive a form, but a http w bodyrequest
        return new ResponseEntity<Company>(companyService.create(company), HttpStatus.CREATED);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@Valid @RequestBody Company company, @PathVariable Integer id){
        if(companyService.optionalFindById(id).isPresent()){
            company.setId(id);
            return new ResponseEntity<Company>(companyService.edit(company), HttpStatus.OK);
        }
        return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Company> delete(@Valid @PathVariable Integer id){
        if(companyService.optionalFindById(id).isPresent()){
            companyService.deleteById(id);
            return new ResponseEntity<Company>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
    }


}
