package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Project;
import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.repository.RoleRepo;
import org.lessons.exam.spring_examprojectmanager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }


    public Boolean boolExistsById(Integer id){
        return userRepo.existsById(id);
    }
    public Boolean boolExists(User userAskIfExists){  //more compact boolExistsById(companyAskIfExists.getId()) but i wanted to show this logic as well
        Optional<User> userOptional = userRepo.findById(userAskIfExists.getId());
        if(userOptional.isPresent()){return true;}
        else{return false;}
    }
    public Optional<User> optionalFindById(Integer id){
        return userRepo.findById(id);
    }
    public User checkedExistsById(Integer id){
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            throw new ResourceNotFoundException("User not found, this exception is in Service checkedExistsById");
        } 
    }



    //READ
    public List<User> findAll(){
        return userRepo.findAll();
    }

    public User getById(Integer id){
        User userFound = checkedExistsById(id);
        return userFound;
    }

    //CREATE
    public User create(User userToCreate){
        if(userToCreate == null){
            throw new IllegalArgumentException("User to create cannot be null.");
        }
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));  //hash before saving!

        //Validate roles getting them from DB
        Set<Role> validRoles = userToCreate.getRoles().stream()
            .map(role -> roleService.checkedExistsById(role.getId()))
            .collect(Collectors.toSet());  //.collect() put the results in a new collection, .toSet to specifi collection Set<>
        for (Role role : validRoles) {
            role.getUsers().add(userToCreate);  //add this user to the obj role
        }
        userToCreate.setRoles(validRoles);
        System.out.println("CreatedUser...: " + userToCreate);
        User savedUser = userRepo.save(userToCreate);
        System.out.println("Saved user from DB: " + userRepo.findById(savedUser.getId()).get()); //x debug GET DATA FROM THE DB!

        return savedUser;
    }


    // UPDATE
    public User edit(User userToEdit){
        if(userToEdit == null){
            throw new IllegalArgumentException("User to update cannot be null.");
        }

        User existingUser = checkedExistsById(userToEdit.getId());

        existingUser.setUsername(userToEdit.getUsername());

        if (!userToEdit.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userToEdit.getPassword()));
        }

        // Update roles
        Set<Role> validRoles = userToEdit.getRoles().stream()
            .map(role -> roleRepo.findById(role.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + role.getId())))
            .collect(java.util.stream.Collectors.toSet());
        existingUser.setRoles(validRoles);

        return userRepo.save(existingUser);
    }

    // DELETE
    public void delete(User userToDelete){
        if(userToDelete == null){
            throw new IllegalArgumentException("User to delete cannot be null.");
        }
        userRepo.delete(userToDelete);
    }

    public void deleteById(Integer id){
        User userToDelete = checkedExistsById(id);
        userRepo.delete(userToDelete);
    }

}
