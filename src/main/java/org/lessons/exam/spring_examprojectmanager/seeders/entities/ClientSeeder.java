package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientSeeder {
    
    private final ClientRepo clientRepo;
    @Autowired
    public ClientSeeder(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public void seed() {
        if(clientRepo.count() == 0){
            List<Client> clients = new ArrayList<>(Arrays.asList(

            new Client(null, "PREMIUM", "ACTIVE", 
                LocalDate.of(2024, 1, 15), 
                LocalDate.of(2025, 1, 15), 
                LocalDateTime.now(), 
                LocalDateTime.now(),
                new Company(null, "ACME Corp", "acme", "12-3456789", "123-4567-8", null, new ArrayList<>(), new ArrayList<>()),
                new Person()
            ),

            new Client(null, "ENTERPRISE", "ACTIVE", 
                LocalDate.of(2024, 3, 1), 
                LocalDate.of(2025, 3, 1), 
                LocalDateTime.now(), 
                LocalDateTime.now(), 
                new Company(null, "Gamma Solutions", "gamma_solutions", "23-4567890", "987-6543-8", null, new ArrayList<>(), new ArrayList<>()),
                new Person()
            ),

            new Client(null, "TRIAL", "PENDING", 
                LocalDate.of(2024, 4, 1), 
                LocalDate.of(2025, 6, 1), 
                LocalDateTime.now(), 
                LocalDateTime.now(), 
                new Company(null, "Delta Innovations", "delta_innov", "34-5678901", "345-6789-0", null, new ArrayList<>(), new ArrayList<>()),
                new Person()
            )

            ));
            clientRepo.saveAll(clients);
        }
    }

}
