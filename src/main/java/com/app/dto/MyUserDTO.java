package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MyUserDTO implements UserDetails {

    UserDTO user;
    List<RoleDTO> roles;

    public MyUserDTO(UserDTO user, List<RoleDTO> roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grant = new HashSet<>();
        roles.forEach(role -> grant.add(new SimpleGrantedAuthority(role.getRoleNm())));
        return grant;
    }

    @Override
    public String getPassword() {
        return user.getUserPwd();
    }

    @Override
    public String getUsername() {
        return user.getUserNm();
    }
}
