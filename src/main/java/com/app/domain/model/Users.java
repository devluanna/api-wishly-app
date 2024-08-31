package com.app.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_user;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private Date date_birthday;
    private String gender;
    private String password;
    private String confirm_password;
    private UserRole role;
    private Status status;
    private String code_recovery_password;
    private String code_account_activation;
    private String confirm_code_activation;

    public Users (String first_name, String last_name, String username, String email, Date date_birthday, String gender, String password, String confirm_password, UserRole role, Status status) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.date_birthday = date_birthday;
        this.gender = gender;
        this.password = password;
        this.confirm_password = confirm_password;
        this.role = UserRole.valueOf("USER");
        this.status = Status.valueOf("ACTIVATED");
    }


    public Users(String username, String email) {
        this.username = username;
        this.email = email;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
