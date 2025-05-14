package org.lessons.exam.spring_examprojectmanager.dto;

import java.time.LocalDateTime;

import org.lessons.exam.spring_examprojectmanager.models.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer taskId;
    private Integer personId;

    public MessageDTO(Message message){  //constructor 1param
        this.id = message.getId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.taskId = message.getTask() != null ? message.getTask().getId() : null;
        this.personId = message.getPerson() != null ? message.getPerson().getId() : null;
    }
    
}
