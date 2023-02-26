package com.hieu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


    @Entity
    @Table(name = "USERS",uniqueConstraints =
            {@UniqueConstraint(columnNames = {"username"}),
                    @UniqueConstraint(columnNames = {"email"})})
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(min = 3, max =25)
        private String name;

        @NotBlank
        @Size(min=3,max = 30)
        private String username;

        @NotBlank
        @Size(max=50)
        @Email
        private String email;

        @JsonIgnore
        @NotBlank
        @Size(min=10,max = 50)
        private String password;
        //    tick long text: image
        @Lob
        private String avatar;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name="user_role", joinColumns =@JoinColumn(name="user_id")
                ,inverseJoinColumns = @JoinColumn(name ="role_id"))
        Set<Role> roles=new HashSet<>();

        public User(@NotBlank
                    @Size(min = 3, max =25) String name,
                    @NotBlank
                    @Size(min=3,max = 30)String username,
                    @NotBlank
                    @Size(max=50)
                    @Email String email,
                    @NotBlank
                    @Size(min=10,max = 50) String encode) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = encode;
        }

        public User(Long id, String name, String username, String email, String password, String avatar, Set<Role> roles) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = password;
            this.avatar = avatar;
            this.roles = roles;
        }

        public User() {

        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Set<Role> getRoles() {
            return roles;
        }

        public void setRoles(Set<Role> roles) {
            this.roles = roles;
        }
    }


