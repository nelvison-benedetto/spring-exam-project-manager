package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Client;
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
                new ArrayList<>()
            ),

            new Client(null, "BASIC", "INACTIVE", 
                LocalDate.of(2024, 5, 10), 
                LocalDate.of(2025, 5, 10), 
                LocalDateTime.now(), 
                LocalDateTime.now(), 
                new ArrayList<>()
            ),

            new Client(null, "ENTERPRISE", "ACTIVE", 
                LocalDate.of(2024, 3, 1), 
                LocalDate.of(2025, 3, 1), 
                LocalDateTime.now(), 
                LocalDateTime.now(), 
                new ArrayList<>()
            ),

            new Client(null, "TRIAL", "PENDING", 
                LocalDate.of(2024, 4, 1), 
                LocalDate.of(2025, 6, 1), 
                LocalDateTime.now(), 
                LocalDateTime.now(), 
                new ArrayList<>()
            )

            ));
            clientRepo.saveAll(clients);
        }
    }

}
