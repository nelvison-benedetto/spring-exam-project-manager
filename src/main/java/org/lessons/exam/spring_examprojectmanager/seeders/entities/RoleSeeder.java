package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder {
    
    private final RoleRepo roleRepo;
    @Autowired
    public RoleSeeder(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public void seed(){
        //if(roleRepo.count() != 0){  //in UserSeeder already created User and Admin
            List<Role> rolesToSeed = List.of(
                //new Role(null, "User", null),
                new Role(null, "Manager", null),
                new Role(null, "Project_Lead", null),
                new Role(null, "Developer", null),
                new Role(null, "Hr", null)
            );
            roleRepo.saveAll(rolesToSeed);
        //}
    }
}
