package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.dto.PersonDTO;
import org.lessons.exam.spring_examprojectmanager.dto.TaskDTO;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.repository.PersonRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.CompanyService;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.lessons.exam.spring_examprojectmanager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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
@RequestMapping("/api/persons")
public class PersonRestController {
    
    private final PersonService personService;
    private final ProjectService projectService;
    private final CompanyService companyService;
    private final PersonRepo personRepo;
    
    @Autowired
    public PersonRestController(PersonService personService, ProjectService projectService, CompanyService companyService, PersonRepo personRepo) {
        this.personService = personService;
        this.projectService = projectService;
        this.companyService = companyService;
        this.personRepo = personRepo;
    }

    //READ
    @GetMapping
    public ResponseEntity<List<PersonDTO>> personsRestIndex(){   //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        List<Person> persons = personRepo.findAll();
        List<PersonDTO> personDTOs = persons.stream().map(PersonDTO::new).toList();  //.map() esegue new PersonDTO(person)
        return ResponseEntity.ok(personDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> personsRestShow(@PathVariable Integer id){   //ResponseEntity<T> classe spring contains body(la response)-statusCodeHttp-opzionaliHeaderCustom(e.g.Content-Type)
        if(!personService.boolExistsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Person> person = personRepo.findById(id);
        PersonDTO personDTO = new PersonDTO(person.get());
        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    //CREATE
    // @PostMapping
    // public ResponseEntity<Person> personsRestStore(@Valid @RequestBody Person person, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     Person personStored = personService.create(person);
    //     return new ResponseEntity<>(personStored, HttpStatus.CREATED);
    // }

    // //UPDATE
    // @PutMapping("/{id}")
    // public ResponseEntity<Person> personsRestUpdate(@Valid @RequestBody Person personToUpdate, @PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!personService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     personToUpdate.setId(id);
    //     Person personUpdated = personService.edit(personToUpdate, customUserDetails);
    //     return new ResponseEntity<>(personUpdated, HttpStatus.OK);
    // }

    // //DELETE
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> personsRestDelete(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    //     if(!personService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     personService.deleteById(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }


    // //OTHERS

    // @GetMapping("/searchByForm")
    // public ResponseEntity<List<Person>> personsSearchByForm(
    //     @AuthenticationPrincipal CustomUserDetails customUserDetails,
    //     @RequestParam(value = "projectId", required = false) Integer projectId,
    //     @RequestParam(value = "companyId", required = false) Integer companyId,
    //     @RequestParam(name="email", required = false, defaultValue = "") String email,
    //     @RequestParam(name="phoneNumber", required = false, defaultValue = "") String phoneNumber
    // ){
    //     List<Person> persons = personService.personsSearchByForm(email, phoneNumber, customUserDetails);
    //     // if(projectId != null){
    //     //     Project project = projectService.getByIdNoSecMain(projectId);
    //     //     model.addAttribute("project", project);
    //     // }
    //     // if(companyId != null){
    //     //     Company company = companyService.getByIdNoSecMain(companyId);
    //     //     model.addAttribute("company", company);
    //     // }
    //     // model.addAttribute("persons", persons);
    //     //return "entities/persons/recruit-person.html";
    //     return ResponseEntity.ok(persons);
    // }


    // @GetMapping("/recruit-person")  //REMINDER se riceve projectId allora link project-person, se no link company-person (questo lo setti se arrivi da page persons/{id}). 
    // public void personsRecruitPerson(
    //     @RequestParam(value = "projectId", required = false) Integer projectId,
    //     @RequestParam(value = "companyId", required = false) Integer companyId,
    //     @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model ) {

    //     if(companyId != null){
    //         List<Person> persons = personService.personsFindAllLessMainLessWithoutCompany(customUserDetails); //I obtain only the persons that are not yet linked to any company!!
    //         model.addAttribute("persons", persons);
    //         Company company = companyService.getByIdNoSecMain(companyId);
    //         model.addAttribute("company", company);      
    //     }else{
    //         List<Person> persons = personService.personsFindAllLessMain(customUserDetails);
    //         model.addAttribute("persons", persons);
    //     }
    //     if(projectId != null){
    //         Project project = projectService.getByIdNoSecMain(projectId);
    //         model.addAttribute("project", project);
    //     }
    //     return "entities/persons/recruit-person.html";


    //     //response in json
    //     Map<String, Object> response = new HashMap<>();
    //     if (companyId != null) {
    //         List<Person> persons = personService.personsFindAllLessMainLessWithoutCompany(customUserDetails);
    //         Company company = companyService.getByIdNoSecMain(companyId);
    //         response.put("persons", persons);
    //         response.put("company", company);
    //     } else {
    //         List<Person> persons = personService.personsFindAllLessMain(customUserDetails);
    //         response.put("persons", persons);
    //     }
    //     if (projectId != null) {
    //         Project project = projectService.getByIdNoSecMain(projectId);
    //         response.put("project", project);
    //     }
    //     return ResponseEntity.ok(response);
    // }

}
