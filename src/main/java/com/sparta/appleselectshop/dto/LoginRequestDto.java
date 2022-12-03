package com.sparta.appleselectshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String username;            // 사용자 이름
    private String password;            // 비밀번호
}