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

                new Company(null, "TechNova Solutions", "technova", 
                "12-3456789", "123-4567-8", 
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "DarkOcean Inc", "darkocean", 
                "23-4567890", "234-5678-3",
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
                ),

                new Company(null, "GreenByte LLC", "greenbyte", 
                "98-7654321", "987-6543-2",
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
                ),
                
                new Company(null, "PixelCraft Studios", "pixelcraft", 
                "45-1234567", "456-7890-1",
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
                )
            
            ));
            companyRepo.saveAll(companies);
        }
    }

}
