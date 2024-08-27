package com.app.controller;

import com.app.dto.MyUserDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import com.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@PreAuthorize("hasRole('DEV')")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@Slf4j
@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin(Authentication auth) {
        return auth.getName();
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @ResponseBody
//    @GetMapping("/logout")
//    public String logout() {
//        return "logout";
//    }

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder pwEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }
    @PostMapping("/sign")
    public String sign(@RequestParam Map<String, String> paramMap, HttpServletRequest req) {
        log.info("New User : {} ", paramMap );
        if (paramMap != null){
            String userPwd = paramMap.get("userPwd");
            String userNm = paramMap.get("userNm");
            String userPwd2 = "";
            // 사용자 비밀번호 암호화 처리
            if (userPwd != null) {
                userPwd2 =  pwEncoder.encode(paramMap.get("userPwd"));
            }
            // 사용자 정보 DTO로 변경
            UserDTO userDTO = new UserDTO();
            userDTO.setUserNm(userNm);
            userDTO.setUserPwd(userPwd2);

            // 사용ㅈ자 테이블에 데이터 저장.
            int state = authMapper.saveUser(userDTO);

            if (state == 1){
               log.info ("User : {}", userDTO);

               // 사용자 권한 등록
               state = authMapper.saveRole(userDTO);
                if(state == 1) {
                    // 자동 로그인 기능 추가.
                    if (userDTO.getUserNm() != null){

                        // 사용자 정보 데이터 가져오기
                        UserDetails userDetails = userService.loadUserByUsername(userDTO.getUserNm());

                        // 사용자 권한 목록 가져오기
                        List<RoleDTO> roles = authMapper.findByRoles(userDTO);

                        // 사용자 전체 정보 생성하기
                        MyUserDTO myUserDTO = new MyUserDTO(userDTO, roles);

                        if(userDetails != null) {
                            log.info("UserDetails : {}", userDetails);
                            // 회원가입으로 생성한 계정의 토큰 생성
                            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                                    userPwd2,
                                    userDetails.getAuthorities());
                            // Security 최상위 객체에 token 전달
                            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                            log.info("token : {}", token);

                            log.info("UserDetails: {}", userDetails);
                            log.info("User Password: {}", userPwd);
                            log.info("Encoded Password from DB: {}", userDetails.getPassword());
                            // 실제 인증 수행
                            try {
                                Authentication authResult = authenticationManager.authenticate(token);
                                SecurityContextHolder.getContext().setAuthentication(authResult);
                                log.info("Authenticated Token : {}", authResult);
                                return "redirect:/";
                            } catch (Exception e) {
                                log.error("Authentication failed: {}", e.getMessage());
                                return "redirect:/sign";
                            }
                        }
                    }
                }
            }
        }
        return "redirect:/sign";
    }

}
