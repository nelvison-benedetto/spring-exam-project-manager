package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    
    private final UserService userService;
    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homePage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails != null){
            System.out.println("User authenticated in HomeController: " + customUserDetails.getUsername());
            User user = userService.getById(customUserDetails.getId());
            Person person = user.getPerson();
            List<Project> projects = person.getProjects(); 

            model.addAttribute("user", user);
        }else {
            System.out.println("Logout ok, no user found in HomeController.");
        }
        return "index.html";
    }

}
