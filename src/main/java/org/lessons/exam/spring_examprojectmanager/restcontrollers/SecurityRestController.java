package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class SecurityRestController {
    @PostMapping("/login")
    public ResponseEntity<Void> login(){ //managed by spring
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //fallback
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){  //managed by spring
        return ResponseEntity.ok().build(); // fallback
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDetails> me(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(user);
    }
}
