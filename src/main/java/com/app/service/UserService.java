package com.app.service;

import com.app.dto.MyUserDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Username : {}" , username);
        UserDTO user = authMapper.findbyUserNm(username).orElseThrow(()-> new UsernameNotFoundException("그런 사람 없다."));
        log.info("User : {}", user);
        List<RoleDTO> roles = authMapper.findByRoles(user);
        roles.forEach(System.out::println);

        return new MyUserDTO(user, roles);
    }
}
