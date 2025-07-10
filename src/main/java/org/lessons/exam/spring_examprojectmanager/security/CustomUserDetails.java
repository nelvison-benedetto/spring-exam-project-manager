package org.lessons.exam.spring_examprojectmanager.security;

import java.util.HashSet;
import java.util.Set;
import org.lessons.exam.spring_examprojectmanager.models.User;

import org.lessons.exam.spring_examprojectmanager.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter  //@override of abstract methods from UserDetails!!
public class CustomUserDetails implements UserDetails{  //interface UserDetails MAIN usata x checks utente at login
    
    private final Integer id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;  //interface GrantedAuthority, here puoi aggiungere objs che implementano GA
      //abstract method getAuthorities()(from UserDetails) is type GA, and w Lombock create getter&setter of this

    public CustomUserDetails(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();

        for (Role userRole : user.getRoles()) {
            this.authorities.add(new SimpleGrantedAuthority(userRole.getName()));  //SGA class che completa getAuthority(from GA) e ha anche .equals()
        }  //search flow: @PreAuthorize("hasAuthority('ADMIN')") search 'ADMIN', @PreAuthorize("hasRole('ADMIN')") search 'ROLE_ADMIN' 
              //-> dopo spring itera x each authorities(x l'item usa il type GA!) e controlla w .equals() return true/false per ciascuno
    }
    

    //set all true, not necessary restrictions for the user now!
    @Override
    public boolean isAccountNonExpired(){return true;}
    @Override
    public boolean isAccountNonLocked(){return true;}
    @Override
    public boolean isCredentialsNonExpired(){return true;}
    @Override
    public boolean isEnabled(){return true;}
}
