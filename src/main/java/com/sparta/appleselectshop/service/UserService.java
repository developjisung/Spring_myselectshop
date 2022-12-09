//package com.sparta.appleselectshop.service;
//
//import com.sparta.appleselectshop.dto.LoginRequestDto;
//import com.sparta.appleselectshop.dto.SignupRequestDto;
//import com.sparta.appleselectshop.entity.User;
//import com.sparta.appleselectshop.entity.UserRoleEnum;
//import com.sparta.appleselectshop.jwt.JwtUtil;
//import com.sparta.appleselectshop.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
//
//    @Transactional
//    public void signup(SignupRequestDto signupRequestDto) {
//        String username = signupRequestDto.getUsername();                                   // value setting
//        String password = signupRequestDto.getPassword();
//        String email    = signupRequestDto.getEmail();
//
//        // 회원 중복 확인
//        Optional<User> found = userRepository.findByUsername(username);
//        if (found.isPresent()) {                                                            // isPresent - > found가 null이 아니라면 true 반환
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");                  // isPresent - > Optional class에 존재하는 함수
//        }
//
//        // 사용자 ROLE 확인
//        UserRoleEnum role = UserRoleEnum.USER;
//
//        if (signupRequestDto.isAdmin()) {                                                   // isAdmin 등의 함수는 dto 내 boolean형 필드가 존재할 때
//            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {                    // @Getter를 통해 생성된다.
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
//            }
//            role = UserRoleEnum.ADMIN;
//        }
//
//        User user = new User(username, password, email, role);
//        userRepository.save(user);
//    }
//
////    @Transactional(readOnly = true)
////    public void login(LoginRequestDto loginRequestDto) {
////        String username = loginRequestDto.getUsername();
////        String password = loginRequestDto.getPassword();
////
////        // 사용자 확인
////        User user = userRepository.findByUsername(username).orElseThrow(
////                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
////        );
////
////        // 비밀번호 확인
////        if(!user.getPassword().equals(password)){
////            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
////        }
////    }
//    @Transactional(readOnly = true)
//    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
//        String username = loginRequestDto.getUsername();
//        String password = loginRequestDto.getPassword();
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//        // 비밀번호 확인
//        if(!user.getPassword().equals(password)){
//            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));      // Jwt 생성 후, Web에 반환하는 response의 header에 해당 정보를 저장함.
//    }
//}

package com.sparta.appleselectshop.service;

import com.sparta.appleselectshop.dto.LoginRequestDto;
import com.sparta.appleselectshop.dto.SignupRequestDto;
import com.sparta.appleselectshop.entity.User;
import com.sparta.appleselectshop.entity.UserRoleEnum;
import com.sparta.appleselectshop.jwt.JwtUtil;
import com.sparta.appleselectshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String email = signupRequestDto.getEmail();
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}