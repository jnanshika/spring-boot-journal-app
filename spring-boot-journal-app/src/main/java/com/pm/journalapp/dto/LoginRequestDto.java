package com.pm.journalapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
