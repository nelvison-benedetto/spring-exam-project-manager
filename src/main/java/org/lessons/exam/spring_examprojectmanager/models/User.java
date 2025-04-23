package org.lessons.exam.spring_examprojectmanager.models;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable  {
    
    private static final long serialVersionUID = 1L; 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "username cannot be blank.")
    @Column(unique = true)  //x additional security 
    private String username;

    @Size(min=6, message="password must be at least 6 chars.")
    @NotBlank(message = "password cannot be blank.")
    private String password;

    @Override
    public String toString() {
        return String.format("%s %s", id, username);
    }

    //RELATIONS

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(  
        name = "role_user",  //ordered in alphabetic order X_Y
        joinColumns = @JoinColumn(name="user_id"),  
        inverseJoinColumns = @JoinColumn(name="role_id"),  
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})  //doesn't allow duplicates couples user_id-role_id at db level
        )
    @Size(min = 1, message = "Select at least one role.")
    private Set<Role> roles;  //<set> doesn't allow duplicates (same role linked to same user) 


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(  //x relation one-to-one isn't necessary create a custom table person_user
        name="person_id"
    )
    private Person person;

}
