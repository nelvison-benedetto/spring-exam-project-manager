package org.lessons.exam.spring_examprojectmanager.seeders.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanySeeder {
    
    private final CompanyRepo companyRepo;
    @Autowired
    public CompanySeeder(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public void seed() {
        if(companyRepo.count() == 0){

            List<Company> companies = new ArrayList<>(Arrays.asList(

                new Company(null, "QuantumArc Technologies", "quantumarc", 
                "12-3456789", "123-4567-8", 
                            null, new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "DeepOcean Industries", "deepocean", 
                "23-4567890", "234-5678-3",
                            null, new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "SynthForge AI", "synthforge", 
                "98-7654321", "987-6543-2",
                            null, new ArrayList<>(), new ArrayList<>()
                ),
                
                new Company(null, "SentinelCore Solutions", "sentinelcore", 
                "45-1234567", "456-7890-1",
                            null, new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "DarkNova Intel", "darknova", 
                "63-7910361", "872-5811-6",
                            null, new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "Aetherion Labs", "aetherion", 
                "93-8570340", "690-3514-9",
                            null, new ArrayList<>(), new ArrayList<>()
                )
            
            ));
            companyRepo.saveAll(companies);
        }
    }

}
