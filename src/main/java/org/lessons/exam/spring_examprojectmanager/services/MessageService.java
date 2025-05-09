package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Company;
import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.MessageRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    private final MessageRepo messageRepo;
    private final UserService userService;
    private final SecurityService securityService;
    private final TaskService taskService;

    @Autowired
    public MessageService(MessageRepo messageRepo, UserService userService, SecurityService securityService, TaskService taskService){
        this.messageRepo = messageRepo;
        this.userService = userService;
        this.securityService = securityService;
        this.taskService = taskService;
    }

    public Boolean boolExistsById(Integer id){
        return messageRepo.existsById(id);
    }

    //also needs a projectAskIfExists w Id here!(no obj not yet saved to db so with Id null)
    public Boolean boolExists(Message messageAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<Message> messageOptional = messageRepo.findById(messageAskIfExists.getId());
        if(messageOptional.isPresent()){return true;}
        else{return false;}
    }

    public Optional<Message> optionalFindById(Integer id){
        return messageRepo.findById(id);
    }

    public Message checkedExistsById(Integer id){
        Optional<Message> messageOptional = messageRepo.findById(id);
        if(messageOptional.isPresent()){
            return messageOptional.get();
        }else{
            throw new ResourceNotFoundException("Message not found.");
        } 
    }

    
    //READ

    @PreAuthorize("isAuthenticated()")
    public Message getByIdNoSecMain(Integer id){
        Message messageFound = getById(id);
        return messageFound;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Message> findAll(){
        return messageRepo.findAll();
    }

    @PreAuthorize("@securityService.hasAccessToMessage(#id, authentication)")
    public Message getById(Integer id){
        Message messageFound = checkedExistsById(id);
        return messageFound;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Message> securityGetAllMessages(Task task, CustomUserDetails customUserDetails){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        List<Message> messages = messageRepo.findByTask(task);
        return messages;
    }

    @PreAuthorize("@securityService.hasAccessToMessage(#id, authentication)")  //run method hasAccessToProject passing params id & customUserDetails logged, if returns true run the method below
    public Message securityGetSingleMessage(Integer id, Task task, CustomUserDetails customUserDetails){
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        Message message = messageRepo.findByIdAndTask(id, task);
        return message;
    }

    //CREATE
    @PreAuthorize("isAuthenticated()")
    public Message create(CustomUserDetails customUserDetails, String content, Integer taskId){
        Task task = taskService.getById(taskId);
        Person person = securityService.checkPersonForActualUser(customUserDetails);
        Message message = new Message();
        message.setContent(content);
        message.setTask(task);
        message.setPerson(person);
        Message savedMessage = messageRepo.save(message);

        System.out.println("Saved message from DB: " + messageRepo.findById(savedMessage.getId()).get()); //x debug GET DATA FROM THE DB!
        return savedMessage;
    }

    //UPDATE
    // @PreAuthorize("isAuthenticated()")
    // public Message create(Message messageToCreate, CustomUserDetails customUserDetails){  //TODO

    // }


    //DELETE
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToMessage(#messageToDelete.id, authentication)")
    public void delete(Message messageToDelete, CustomUserDetails customUserDetails){

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.hasAccessToMessage(#id, authentication)")
    public void deleteById(Integer id, CustomUserDetails customUserDetails){

    }

    //FILTERS


}
