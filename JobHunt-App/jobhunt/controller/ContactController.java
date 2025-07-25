package com.example.jobhunt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.jobhunt.repository.ContactMessageRepository;
import com.example.jobhunt.entity.ContactMessage;

import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    // ✅ Save contact message
    @PostMapping("/contact")
    public String saveContact(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String message,
                              Model model) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(name);
        contactMessage.setEmail(email);
        contactMessage.setMessage(message);
        contactMessageRepository.save(contactMessage);
        model.addAttribute("success", "Your message has been sent!");
        return "contact";
    }

    @GetMapping("/admin/messages")
    public String showAdminMessagesPage(Model model) {
        model.addAttribute("messages", contactMessageRepository.findAll());
        return "admin_messages";
    }


    @GetMapping("/admin/messages/data")
    @ResponseBody
    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAll();
    }
}
