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


@Getter @Setter
public class CustomUserDetails implements UserDetails{
    
    private final Integer id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;  //qua possono venire anche objs che IMPLEMENTATO interface GrantedAuthority

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();

        for (Role userRole : user.getRoles()) {
            this.authorities.add(new SimpleGrantedAuthority(userRole.getName()));  //SGA class che completa hasAuthority(from GrantedAuthority) e ha anche .equals()
        }  //@PreAuthorize("hasAuthority('ADMIN')") search 'ADMIN', @PreAuthorize("hasRole('ADMIN')") search 'ROLE_ADMIN'. dopo utilizzano .equals()
    }
    
    //set all true, not necessary restrictions now!
    @Override
    public boolean isAccountNonExpired(){return true;}
    @Override
    public boolean isAccountNonLocked(){return true;}
    @Override
    public boolean isCredentialsNonExpired(){return true;}
    @Override
    public boolean isEnabled(){return true;}
}
