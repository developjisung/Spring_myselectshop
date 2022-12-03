package com.sparta.appleselectshop.controller;

import com.sparta.appleselectshop.dto.LoginRequestDto;
import com.sparta.appleselectshop.dto.SignupRequestDto;
import com.sparta.appleselectshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // call signup html
    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    // call login html
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }


    // DB insert(User, Admin validate -> DB insert)
    @PostMapping("/signup")
    public String signup(SignupRequestDto signupRequestDto) {                                           // POST 방식인데 왜 어노테이션이 존재하지 않는가?
        userService.signup(signupRequestDto);                                                           // Query String이나 path variable이 아닌 JSON 형식으로 값이 넘어오는 Body 형식인 듯 한대
        return "redirect:/api/user/login";                                                              // 그럼 RequestBody가 존재하거나 @Controller가 아닌 - > @RestController여야 하는 것이 아닌가?
    }

//    @PostMapping("/login")                                                                            // 이거도 보면 동일함.
//    public String login(LoginRequestDto loginRequestDto) {
//        userService.login(loginRequestDto);
//        return "redirect:/api/shop";
//    }

    // Jwt create and return jwt to web
    @ResponseBody                                                                                       // 해당 API는 html을 반환하는 부분이 존재하지 않는다.
    @PostMapping("/login")                                                                           // login html을 들여다보면 response를 받아 받은 정보를 통해, web cookie에 jwt를 저장함.
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {   // return success가 돌아오면 그때 web에서 html을 다른 것으로 변경함.
        userService.login(loginRequestDto, response);                                                   // HttpServletResponse도 동일하게 ResponseBody가 필요하다.
        return "success";
    }

}