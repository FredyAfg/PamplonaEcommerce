package com.codePamplonaEcomerc.pamplonaecomerc.dto;


import com.codePamplonaEcomerc.pamplonaecomerc.entity.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private  String name;
    private UserRole userRole;

}
