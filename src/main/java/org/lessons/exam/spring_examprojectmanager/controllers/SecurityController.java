package org.lessons.exam.spring_examprojectmanager.controllers;

import org.lessons.exam.spring_examprojectmanager.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/security")
public class SecurityController {
    

    @GetMapping("/sign-in")
    public String signInPage(){
        return "security/sign-in.html";
    }

    //@PostMapping("/sign-in") managed auto by spring security!

    @GetMapping("/sign-up")
    public String signUpPage(Model model){
        model.addAttribute("user", new User());
        //model.addAttribute("roles", List.of("USER", "MANAGER", "DEVELOPER")); // ruoli che permetti in signup
        //<option th:each="role : ${roles}" th:value="${role}" th:text="${role}"></option>
        return "security/sign-up.html";
    }

    
}
