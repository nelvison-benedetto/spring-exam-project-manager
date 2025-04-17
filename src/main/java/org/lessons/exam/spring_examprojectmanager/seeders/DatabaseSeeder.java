package org.lessons.exam.spring_examprojectmanager.seeders;

import org.lessons.exam.spring_examprojectmanager.seeders.entities.ClientSeeder;
import org.lessons.exam.spring_examprojectmanager.seeders.entities.CompanySeeder;
import org.lessons.exam.spring_examprojectmanager.seeders.entities.ProjectSeeder;
import org.lessons.exam.spring_examprojectmanager.seeders.entities.RoleSeeder;
import org.lessons.exam.spring_examprojectmanager.seeders.entities.TaskSeeder;
import org.lessons.exam.spring_examprojectmanager.seeders.entities.UserSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner{
    
    private final ClientSeeder clientSeeder;
    private final CompanySeeder companySeeder;
    private final ProjectSeeder projectSeeder;
    private final TaskSeeder taskSeeder;
    private final UserSeeder userSeeder;
    private final RoleSeeder roleSeeder;
    
    public DatabaseSeeder(ClientSeeder clientSeeder, CompanySeeder companySeeder, ProjectSeeder projectSeeder, TaskSeeder taskSeeder, UserSeeder userSeeder, RoleSeeder roleSeeder){
        this.clientSeeder = clientSeeder;
        this.companySeeder = companySeeder;
        this.projectSeeder = projectSeeder;
        this.taskSeeder = taskSeeder;
        this.userSeeder = userSeeder;
        this.roleSeeder = roleSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        //the order is important!
        projectSeeder.seed();  //has relation many-one w nullable=true, so in task you cannot set null in the project field!
        taskSeeder.seed();

        //clientSeeder.seed();
        companySeeder.seed();
        userSeeder.seed();
        roleSeeder.seed();
    }
}
