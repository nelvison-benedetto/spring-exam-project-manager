package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.dto.MessageDTO;
import org.lessons.exam.spring_examprojectmanager.dto.TaskDTO;
import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {
    
    private final MessageRepo repoMessage;

    @Autowired
    public MessageRestController(MessageRepo repoMessage) {
        this.repoMessage = repoMessage;
    }

    //READ
    @GetMapping
    public ResponseEntity<List<MessageDTO>> messagesRestIndex() {
        List<Message> messages = repoMessage.findAll();
        List<MessageDTO> messageDTOs = messages.stream().map(MessageDTO::new).toList();
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> messagesRestShow(@PathVariable Integer id) {
        if(!repoMessage.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Message> message = repoMessage.findById(id);
        MessageDTO messageDTO = new MessageDTO(message.get());
        return new ResponseEntity<>(messageDTO, HttpStatus.OK);
    }
    
}
