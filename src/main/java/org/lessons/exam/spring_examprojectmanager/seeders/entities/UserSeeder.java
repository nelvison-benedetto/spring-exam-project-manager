package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.PersonRepo;
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
    private PersonRepo personRepo;

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
        Optional<User> tocheckAdminUser = userRepo.findByUsername("userAdmin");
        if (! tocheckAdminUser.isPresent()) {
            User adminUser = new User();
            adminUser.setUsername("userAdmin");
            adminUser.setPassword(passwordEncoder.encode("12345678"));  //encryption applied
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);     //add role ADMIN
            adminUser.setRoles(adminRoles);
            //userRepo.save(adminUser);
            
            // Creazione e associazione della Person all'utente Admin
            Person adminPerson = new Person();
            adminPerson.setFirstname("Admin");
            adminPerson.setLastname("User");
            adminPerson.setEmail("admin@example.com");
            adminPerson.setPhoneNumber("1234567890");
            adminPerson.setCountry("Italy");
            adminPerson.setBirthdate(LocalDate.of(1990, 1, 1));

            adminPerson.setUser(adminUser);  // Collega la Person all'User
            adminUser.setPerson(adminPerson);  // Collega l'User alla Person

            userRepo.save(adminUser);
        }

        Optional<User> tocheckNoAdminUser = userRepo.findByUsername("userNoAdmin");
        if (!tocheckNoAdminUser.isPresent()) {

            User normalUser = new User();
            normalUser.setUsername("userNoAdmin");
            normalUser.setPassword("{noop}12345678");   //!noop x Password NO encryption, only x didactic purposes
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);     //add role USER
            normalUser.setRoles(userRoles);
            //userRepo.save(normalUser);

            // Creazione e associazione della Person all'utente non Admin
            Person normalPerson = new Person();
            normalPerson.setFirstname("NoAdmin");
            normalPerson.setLastname("User");
            normalPerson.setEmail("noadmin@example.com");
            normalPerson.setPhoneNumber("0987654321");
            normalPerson.setCountry("Italy");
            normalPerson.setBirthdate(LocalDate.of(1995, 5, 5));

            normalPerson.setUser(normalUser);  // Collega la Person all'User
            normalUser.setPerson(normalPerson);  // Collega l'User alla Person

            userRepo.save(normalUser);

        }
    }

}
