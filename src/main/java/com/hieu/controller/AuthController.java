package com.hieu.controller;

import com.hieu.dto.request.SignInForm;
import com.hieu.dto.request.SignUpForm;
import com.hieu.dto.response.JwtResponse;
import com.hieu.dto.response.ResponseMessage;
import com.hieu.model.Role;
import com.hieu.model.RoleName;
import com.hieu.model.User;
import com.hieu.security.jwt.JwtProvider;
import com.hieu.security.userprincal.UserPrinciple;
import com.hieu.service.impl.RoleServiceImpl;
import com.hieu.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("The user is existed"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.OK);
        }
        User user = new User(signUpForm.getName(),signUpForm.getUsername(),signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles =new HashSet<>();
        strRoles.forEach(role->{
            switch (role){
                case "admin":
                    Role adminRole =roleService.findByName(RoleName.ADMIN).orElseThrow(()->new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole =roleService.findByName(RoleName.PM).orElseThrow(()->new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                case "user":
                    Role userRole =roleService.findByName(RoleName.USER).orElseThrow(()->new RuntimeException("Role not found"));
                    roles.add(userRole);
                    break;
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("Create success"),HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(),signInForm.getPassword())

        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.creatToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token,userPrinciple.getName(),userPrinciple.getAuthorities()));

    }
}
