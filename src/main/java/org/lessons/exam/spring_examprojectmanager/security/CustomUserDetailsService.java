package org.lessons.exam.spring_examprojectmanager.security;

import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> userOptional = userRepo.findByUsername(username);
        if(userOptional.isPresent()){
            System.out.println("User found w .loadUserByUsername: " + username);  //x debug
            return new CustomUserDetails(userOptional.get());
        }
        else{
            throw new UsernameNotFoundException("No user not found with username "+username);
        }
    }
}
