package org.lessons.exam.spring_examprojectmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.lessons.exam.spring_examprojectmanager.models.Client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    
    private Integer id;
    private String subscriptionType;
    private String status;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Integer companyId;
    private Integer personId;

    public ClientDTO(Client client){  //constructor 1param
        this.id = client.getId();
        this.subscriptionType = client.getSubscriptionType();
        this.status = client.getStatus();
        this.subscriptionStartDate = client.getSubscriptionStartDate();
        this.subscriptionEndDate = client.getSubscriptionEndDate();
        this.createdAt = client.getCreatedAt();
        this.updatedAt = client.getUpdatedAt();
        
        this.companyId = client.getCompany() != null ? client.getCompany().getId() : null;
        this.personId = client.getPerson() != null ? client.getPerson().getId() : null;
    }

    
}
