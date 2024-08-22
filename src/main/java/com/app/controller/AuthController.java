package com.app.controller;

import com.app.dto.LoginDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
public class AuthController {

    @Autowired
    private AuthMapper authMapper;

//    @PostMapping("/login")
//    public String login(@RequestBody LoginDTO loginDTO) {
//        log.info("Login : {}", loginDTO);
//        UserDTO userDTO =  authMapper.login(loginDTO);
//        log.info("Login User : {}", userDTO);
//        if(userDTO == null) {
//            return "그런 유저 없음 !";
//        } else {
//            return "로그인 성공";
//        }
//    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestParam Map<String, String> loginDTO) {
        log.info("login : {}", loginDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(false);
        UserDTO userDTO =  authMapper.login2(loginDTO);
        log.info("Login User : {}", userDTO);

        // 입력한 사용자 정보가 없을 경우 에러 리턴
        if(userDTO == null) {
            return responseDTO;
        }
        // 입력한 사용자 정보가 있을 경우 권한 조회 및 리턴
        List<RoleDTO> roles = authMapper.findByRoles(userDTO);
        responseDTO.setStatus(true);
        responseDTO.setUser(userDTO);
        responseDTO.setRoles(roles);
        return responseDTO;

    }


}
