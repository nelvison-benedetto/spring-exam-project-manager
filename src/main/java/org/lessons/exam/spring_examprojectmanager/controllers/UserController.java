package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.services.RoleService;
import org.lessons.exam.spring_examprojectmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //READ
    @GetMapping
    public String usersIndex(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "entities/users/index.html";
    }

    @GetMapping("/{id}")
    public String usersShow(@PathVariable Integer id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "entities/users/show.html";
    }


    //CREATE
    @GetMapping("/create")
    public String usersCreate(Model model) {
        model.addAttribute("user", new User());
        List<Role> allRoles = roleService.findAll();  //x debug
        model.addAttribute("rolesList", allRoles);
        return "security/sign-up.html";
    }

    @PostMapping("/store")
    public String usersStore(@Valid @ModelAttribute("user") User user,
    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rolesList", roleService.findAll());
            return "security/sign-up.html";
        }
        userService.create(user);
        return "redirect:/persons/create";
    }

    //UPDATE
    @GetMapping("/{id}/edit")
    public String usersEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("edit", true);
        return "entities/users/create-or-edit.html";
    }

    @PutMapping("/{id}/update")
    public String usersUpdate(@Valid @ModelAttribute("user") User user,
    BindingResult bindingResult, @PathVariable Integer id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "entities/users/create-or-edit.html";
        }
        userService.edit(user);
        return "redirect:/users/" + id;
    }

    // DELETE
    @DeleteMapping("/{id}/delete")
    public String usersDelete(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

}
