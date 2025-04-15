package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.util.HashSet;
import java.util.Set;

import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.RoleRepo;
import org.lessons.exam.spring_examprojectmanager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seed() {

        //creations Role entities
        Role adminRole = new Role();
        adminRole.setName("Admin");
        Role userRole = new Role();
        userRole.setName("User");

        roleRepo.save(adminRole);
        roleRepo.save(userRole);

        //creations User entitites
        User adminUser = new User();
        adminUser.setUsername("personAdmin");
        adminUser.setPassword(passwordEncoder.encode("12345678"));  //encryption applied
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);     //add role ADMIN
        adminUser.setRoles(adminRoles);
        userRepo.save(adminUser);

        User normalUser = new User();
        normalUser.setUsername("personNoAdmin");
        normalUser.setPassword("{noop}12345678");   //!noop x Password NO encryption, only x didactic purposes
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);     //add role USER
        normalUser.setRoles(userRoles);
        userRepo.save(normalUser);

    }

}
