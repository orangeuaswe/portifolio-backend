package com.portifolio.portifolio.controller;

import com.portifolio.portifolio.model.ContactRequest;
import com.portifolio.portifolio.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ContactController
{
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<Map<String,String>> handleContactForm(@Valid @RequestBody ContactRequest contactRequest)
    {
        Map<String,String> response = new HashMap<>();
        try
        {
            logger.info("Received contact form submission from: {}", contactRequest.getEmail());
            emailService.sendContactEmail(contactRequest);
            response.put("status","success");
            response.put("message","Your message has been sent successfully!");
            logger.info("Contact email sent successfully to: {}", contactRequest.getEmail());
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error sending contact email", e);
            response.put("status","error");
            response.put("message","There was an error sending your message. Please try again later.");
            return ResponseEntity.status(500).body(response);
        }
    }
    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> healthCheck()
    {
        Map<String,String> response = new HashMap<>();
        response.put("status","success");
        response.put("message","API is running!");
        return ResponseEntity.ok(response);
    }
}