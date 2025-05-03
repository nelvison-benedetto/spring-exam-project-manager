package org.lessons.exam.spring_examprojectmanager.advice;

import java.security.Security;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.PersonService;
import org.lessons.exam.spring_examprojectmanager.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    private final PersonService personService;
    private final SecurityService securityService;

    @Autowired
    public GlobalControllerAdvice(PersonService personService, SecurityService securityService) {
        this.personService = personService;
        this.securityService = securityService;
    }

    @ModelAttribute
    public void addPersonToModel(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails != null){
            //System.out.println("CustomUserDetails FROM GLOBAL ADVICE: " + customUserDetails);
            Person personHeader = securityService.checkPersonForActualUser(customUserDetails);
            if(personHeader != null){
                model.addAttribute("personHeader", personHeader);
            }
        }
    }
}
