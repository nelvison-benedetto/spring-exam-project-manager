package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.dto.CompanyDTO;
import org.lessons.exam.spring_examprojectmanager.dto.TaskDTO;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController  //= @Controller + @ResponseBody(serializza in json format)
@RequestMapping("/api/companies")
public class CompanyRestController {
    
    private final CompanyService companyService;
    private final PersonService personService;
    private final ProjectService projectService;
    private final CompanyRepo companyRepo;

    public CompanyRestController(CompanyService companyService, PersonService personService, ProjectService projectService, CompanyRepo companyRepo) {
        this.companyService = companyService;
        this.personService = personService;
        this.projectService = projectService;
        this.companyRepo = companyRepo;
    }


    //READ
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> companiesRestIndex() {  //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        List<Company> companies = companyRepo.findAll();
        List<CompanyDTO> companykDTOs = companies.stream().map(CompanyDTO::new).toList();  //.map() esegue new CompanyDTO(company)
        return ResponseEntity.ok(companykDTOs);  //return body+statusCode
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> companiesRestShow(@PathVariable Integer id){  //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        if(!companyService.boolExistsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Company> company = companyRepo.findById(id);
        CompanyDTO companyDTO = new CompanyDTO(company.get());
        return new ResponseEntity<>(companyDTO, HttpStatus.OK); //return body+statusCode(here puoi specificare lo status manualmente better)
    }
    

    //CREATE
    // @PostMapping
    // public ResponseEntity<Company> companiesRestStore(@Valid @RequestBody Company companyToUpdate, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     Company companyStored = companyService.create(companyToUpdate);
    //     return new ResponseEntity<>(companyStored, HttpStatus.CREATED);
    // }

    // //UPDATE
    // @PutMapping("/{id}")
    // public ResponseEntity<Company> companiesRestUpdate(@Valid @RequestBody Company companyToUpdate, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!companyService.boolExistsById(id)){
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     companyToUpdate.setId(id);
    //     Company companyUpdated = companyService.edit(companyToUpdate, customUserDetails);
    //     return new ResponseEntity<>(companyUpdated, HttpStatus.OK);
    // }

    // //DELETE
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> companiesRestDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!companyService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     companyService.deleteById(id, customUserDetails);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

    // //OTHERS

    // @GetMapping("/searchByForm")
    // public ResponseEntity<?> companiesSearchByForm(
    //         @AuthenticationPrincipal CustomUserDetails customUserDetails,
    //         @RequestParam(value = "projectId", required = false) Integer projectId,
    //         @RequestParam(name = "companyLegalName", required = false, defaultValue = "") String companyLegalName,
    //         @RequestParam(name = "companyUsername", required = false, defaultValue = "") String companyUsername) {
    //     List<Company> companies = companyService.companiesSearchByForm(companyLegalName, companyUsername, customUserDetails);
    //     if (projectId != null) {
    //         return ResponseEntity.ok(
    //                 new Object() {
    //                     public final List<Company> companiesList = companies;
    //                     public final Object project = projectService.getByIdNoSecMain(projectId);
    //                 });
    //     }
    //     return ResponseEntity.ok(companies);
    // }

}
