package org.lessons.exam.spring_examprojectmanager.services;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.exceptions.ResourceNotFoundException;
import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.lessons.exam.spring_examprojectmanager.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    
    private final RoleRepo roleRepo;
    @Autowired 
    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> findAll() {
        return roleRepo.findAll();
    }
    public Role checkedExistsById(Integer id) {
        return roleRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found."));
    }

}
