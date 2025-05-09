package org.lessons.exam.spring_examprojectmanager.controllers;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.models.Person;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.lessons.exam.spring_examprojectmanager.repository.MessageRepo;
import org.lessons.exam.spring_examprojectmanager.security.CustomUserDetails;
import org.lessons.exam.spring_examprojectmanager.services.MessageService;
import org.lessons.exam.spring_examprojectmanager.services.SecurityService;
import org.lessons.exam.spring_examprojectmanager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/messages")
public class MessageController {
    
    private final MessageService messageService;
    private final TaskService taskService;
    private final SecurityService securityService;
    private final MessageRepo messageRepo;

    @Autowired
    public MessageController(MessageService messageService, TaskService taskService, SecurityService securityService, MessageRepo messageRepo) {
        this.messageService = messageService;
        this.taskService = taskService;
        this.securityService = securityService;
        this.messageRepo = messageRepo;
    }

    //READ
    @GetMapping
    public String messagesIndex(@RequestParam(value="taskId", required = false) Integer taskId, Model model,
    @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Person actualperson = securityService.checkPersonForActualUser(customUserDetails);
        Task task = taskService.getById(taskId);
        List<Message> messages = messageService.securityGetAllMessages(task, customUserDetails);
        model.addAttribute("messages", messages);
        model.addAttribute("task", task);
        model.addAttribute("actualperson", actualperson);
        return "entities/messages/index.html";
    }

    //CREATE
    @PostMapping("/create")
    public String createMessage(
            @RequestParam("taskId") Integer taskId,
            @RequestParam("content") String content,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Message savedMessage = messageService.create(customUserDetails, content, taskId);
        return "redirect:/messages?taskId=" + taskId;
    }


    //UPDATE
    @GetMapping("/{id}/edit")
    public String messagesUpdate(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        Message noSecurityMessage = messageService.getById(id);
        Message securityMessage = messageService.securityGetSingleMessage(id, noSecurityMessage.getTask(), customUserDetails);
        model.addAttribute("message", securityMessage );
        model.addAttribute("edit", true);
        return "entities/messages/create-or-edit.html";
    }



}
