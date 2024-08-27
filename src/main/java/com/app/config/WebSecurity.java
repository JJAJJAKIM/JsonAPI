package com.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
//@EnableMethodSecurity
@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurity {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.disable());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(req -> {
           req.requestMatchers("/**").permitAll();
//           req.requestMatchers("/","/token","/admin","/sign").permitAll();
//           req.requestMatchers("/admin").hasRole("ADMIN");
//            req.requestMatchers("/token").hasRole("USER");
//           req.anyRequest().authenticated();
        });

        http.formLogin(
//                Customizer.withDefaults()
                (formLogin) -> formLogin.loginPage("/login")
                /* 로그인 페이지에서 사용할 파라미터의 이름을 정의 한다. 기본이름은 username, password이다.*/
                        .usernameParameter("userNm")
                        .passwordParameter("userPwd")
                /* ----------------------------------------------------------------------- */

                /* 로그인 기능 동작시 성공 또는 실패 이후 이동할 페이지 지정.(Handler보다 우선순위가 낮아서 같이 사용한다면 handler 설정으로 동작한다 */
                        .defaultSuccessUrl("/",false)
                        // false : 접속했던 페이지로 돌아간다. true : 무조건 기본 설정 페이지로 간다.
//                        .failureUrl("/login?error")
                /**********************************************************************************************************/

                /* 로그인 기능 동작시 성공 또는 실패 이후 이동할 페이지를 Handler를 사용하여 지정한다. */
//                        .successHandler((req, resp, auth) -> {
//                            log.info("User logged in: {}", auth.getPrincipal());
//                            resp.sendRedirect("/");
//                        })
//                        .failureHandler((request, response, exception) ->
//                                exception.printStackTrace()
//                                )
//                        .permitAll()
                /**********************************************************************************************************/
//                   .failureUrl("/fail")
                        .permitAll()

        );

        return http.build();
    }
//
//    @Bean
//    public UserDetailsService users() {
//        log.info("Users pwd : {}", passwordEncoder().encode("1234") );
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("1234"))
//                .roles("DEV")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("1234"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
