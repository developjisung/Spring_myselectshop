//package com.sparta.appleselectshop.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping("/api")
//public class ShopController {
//
//    @GetMapping("/shop")
//    public ModelAndView shop() {
//        ModelAndView modelAndView = new ModelAndView("index");
//        modelAndView.addObject("username","");
//        return modelAndView;
//    }
//}

//package com.sparta.appleselectshop.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping("/api")
//public class ShopController {
//
//    // call main html
//    @GetMapping("/shop")
//    public ModelAndView shop() {
//        return new ModelAndView("index");
//    }
//}

//package com.sparta.appleselectshop.controller;
//
//import com.sparta.appleselectshop.service.FolderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class ShopController {
//
//    private final FolderService folderService;
//
//    @GetMapping("/shop")
//    public ModelAndView shop() {
//        return new ModelAndView("index");
//    }
//
//    // 로그인 한 유저가 메인페이지를 요청할 때 가지고있는 폴더를 반환
//    @GetMapping("/user-folder")
//    public String getUserInfo(Model model, HttpServletRequest request) {
//
//        model.addAttribute("folders", folderService.getFolders(request));
//
//        return "/index :: #fragment";
//    }
//
//}

package com.sparta.appleselectshop.controller;

import com.sparta.appleselectshop.entity.User;
import com.sparta.appleselectshop.jwt.JwtUtil;
import com.sparta.appleselectshop.repository.UserRepository;
import com.sparta.appleselectshop.service.FolderService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopController {

    private final FolderService folderService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/shop")
    public ModelAndView shop() {
        return new ModelAndView("index");
    }

    // 로그인 한 유저가 메인페이지를 요청할 때 가지고있는 폴더를 반환
    @GetMapping("/user-folder")
    public String getUserInfo(Model model, HttpServletRequest request) {

        model.addAttribute("folders", folderService.getFolders(request));

        return "index :: #fragment";
    }

    // 로그인 한 유저가 메인페이지를 요청할 때 유저의 이름 반환
    @GetMapping("/user-info")
    @ResponseBody
    public String getUserName(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            return user.getUsername();
        }else {
            return "fail";
        }
    }
}