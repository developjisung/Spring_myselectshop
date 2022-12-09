////package com.sparta.appleselectshop.service;
////
////import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
////import com.sparta.appleselectshop.dto.ProductRequestDto;
////import com.sparta.appleselectshop.dto.ProductResponseDto;
////import com.sparta.appleselectshop.entity.Product;
////import com.sparta.appleselectshop.repository.ProductRepository;
////import org.springframework.stereotype.Component;
////
////import java.sql.SQLException;
////import java.util.List;
////
////@Component
////public class ProductService {
////
////    public ProductResponseDto createProduct(ProductRequestDto requestDto) throws SQLException {
////        // 요청받은 DTO 로 DB에 저장할 객체 만들기
////        Product product = new Product(requestDto);
////
////        ProductRepository productRepository = new ProductRepository();
////
////        return  productRepository.createProduct(product);
////    }
////
////    public List<ProductResponseDto> getProducts() throws SQLException {
////        ProductRepository productRepository = new ProductRepository();
////
////        return productRepository.getProducts();
////    }
////
////    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
////        ProductRepository productRepository = new ProductRepository();
////        Product product = productRepository.getProduct(id);
////
////        if(product == null) {
////            throw new NullPointerException("해당 상품은 존재하지 않습니다.");
////        }
////
////        return productRepository.updateProduct(product.getId(), requestDto);
////    }
////
////}
//
////package com.sparta.appleselectshop.service;
////
////import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
////import com.sparta.appleselectshop.dto.ProductRequestDto;
////import com.sparta.appleselectshop.dto.ProductResponseDto;
////import com.sparta.appleselectshop.entity.Product;
////import com.sparta.appleselectshop.repository.ProductRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Component;
////
////import java.sql.SQLException;
////import java.util.List;
////
////@Component
////@RequiredArgsConstructor
////public class ProductService {
////
////    private final ProductRepository productRepository;
////
////    public ProductResponseDto createProduct(ProductRequestDto requestDto) throws SQLException {
////        // 요청받은 DTO 로 DB에 저장할 객체 만들기
////        Product product = new Product(requestDto);
////
////        return  productRepository.createProduct(product);
////    }
////
////    public List<ProductResponseDto> getProducts() throws SQLException {
////
////        return productRepository.getProducts();
////    }
////
////    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
////        Product product = productRepository.getProduct(id);
////
////        if(product == null) {
////            throw new NullPointerException("해당 상품은 존재하지 않습니다.");
////        }
////
////        return productRepository.updateProduct(product.getId(), requestDto);
////    }
////
////}
//
//package com.sparta.appleselectshop.service;
//
//import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
//import com.sparta.appleselectshop.dto.ProductRequestDto;
//import com.sparta.appleselectshop.dto.ProductResponseDto;
//import com.sparta.appleselectshop.entity.Product;
//import com.sparta.appleselectshop.entity.User;
//import com.sparta.appleselectshop.entity.UserRoleEnum;
//import com.sparta.appleselectshop.jwt.JwtUtil;
//import com.sparta.appleselectshop.naver.dto.ItemDto;
//import com.sparta.appleselectshop.repository.ProductRepository;
//import com.sparta.appleselectshop.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//
//    @Transactional
//    public ProductResponseDto createProduct(ProductRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);                                                       // Client에서 token 묶어서 보낸 것을 jwt에서 뽑아내 token에 저장
//        Claims claims;                                                                                      // Jwt 내에 존재하는 유저 정보 뽑기 위한 변수
//
//        // 토큰이 있는 경우에만 관심상품 추가 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Product product = productRepository.saveAndFlush(new Product(requestDto, user.getId()));        // DTO -> Entity 변환 후 반영
//                                                                                                            // saveAndFlush -> 영속성 컨텍스트에 담지 않고 바로 반영
//
//            return new ProductResponseDto(product);                                                         // Entity -> DTO 변환 후 반환
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional(readOnly = true)                                                                         // Select지만 Transactional을 명시적으로 적어주는게 좋음.
//    public List<ProductResponseDto> getProducts(HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 조회 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            List<ProductResponseDto> list = new ArrayList<>();                                              // DTO list
//            List<Product> productList;                                                                      // Entity list
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                productList = productRepository.findAllByUserId(user.getId());                              // 일반 USER라면, 해당 USER 정보 SELECT
//            } else {
//                productList = productRepository.findAll();                                                  // 관리자라면, 전체 데이터 SELECT
//            }
//
//            for (Product product : productList) {                                                           // Entity -> DTO
//                list.add(new ProductResponseDto(product));
//            }
//
//            return list;                                                                                    // return DTO list
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(                         // User info find
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            Product product = productRepository.findByIdAndUserId(id, user.getId()).orElseThrow(                // 찾은 유저 정보로 product select
//                    () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//            );
//
//            product.update(requestDto);
//
//            return product.getId();                                                                             // Entity의 id 값 받아오기
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public void updateBySearch (Long id, ItemDto itemDto){
//        Product product = productRepository.findById(id).orElseThrow(
//                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//        );
//        product.updateByItemDto(itemDto);
//    }
//
//}

//package com.sparta.appleselectshop.service;
//
//import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
//import com.sparta.appleselectshop.dto.ProductRequestDto;
//import com.sparta.appleselectshop.dto.ProductResponseDto;
//import com.sparta.appleselectshop.entity.Folder;
//import com.sparta.appleselectshop.entity.Product;
//import com.sparta.appleselectshop.entity.User;
//import com.sparta.appleselectshop.entity.UserRoleEnum;
//import com.sparta.appleselectshop.jwt.JwtUtil;
//import com.sparta.appleselectshop.naver.dto.ItemDto;
//import com.sparta.appleselectshop.repository.FolderRepository;
//import com.sparta.appleselectshop.repository.ProductRepository;
//import com.sparta.appleselectshop.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final FolderRepository folderRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//
//    @Transactional
//    public ProductResponseDto createProduct(ProductRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 추가 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Product product = productRepository.saveAndFlush(new Product(requestDto, user.getId()));
//
//            return new ProductResponseDto(product);
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public Page<Product> getProducts(HttpServletRequest request,
//                                     int page, int size, String sortBy, boolean isAsc) {
//        // 페이징 처리
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 조회 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            Page<Product> products;
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                products = productRepository.findAllByUserId(user.getId(), pageable);
//            } else {
//                products = productRepository.findAll(pageable);
//            }
//
//            return products;
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            Product product = productRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
//                    () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//            );
//
//            product.update(requestDto);
//
//            return product.getId();
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public void updateBySearch(Long id, ItemDto itemDto) {
//        Product product = productRepository.findById(id).orElseThrow(
//                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//        );
//        product.updateByItemDto(itemDto);
//    }
//
//    @Transactional
//    public Product addFolder(Long productId, Long folderId, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 1) 상품을 조회합니다.
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new NullPointerException("해당 상품 아이디가 존재하지 않습니다."));
//
//            // 2) 관심상품을 조회합니다.
//            Folder folder = folderRepository.findById(folderId)
//                    .orElseThrow(() -> new NullPointerException("해당 폴더 아이디가 존재하지 않습니다."));
//
//            // 3) 조회한 폴더와 관심상품이 모두 로그인한 회원의 소유인지 확인합니다.
//            Long loginUserId = user.getId();
//            if (!product.getUserId().equals(loginUserId) || !folder.getUser().getId().equals(loginUserId)) {
//                throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다~^^");
//            }
//
//            // 4) 상품에 폴더를 추가합니다.
//            product.addFolder(folder);
//
//            return product;
//        } else {
//            return null;
//        }
//    }
//}

//package com.sparta.appleselectshop.service;
//
//import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
//import com.sparta.appleselectshop.dto.ProductRequestDto;
//import com.sparta.appleselectshop.dto.ProductResponseDto;
//import com.sparta.appleselectshop.entity.Folder;
//import com.sparta.appleselectshop.entity.Product;
//import com.sparta.appleselectshop.entity.User;
//import com.sparta.appleselectshop.entity.UserRoleEnum;
//import com.sparta.appleselectshop.jwt.JwtUtil;
//import com.sparta.appleselectshop.naver.dto.ItemDto;
//import com.sparta.appleselectshop.repository.FolderRepository;
//import com.sparta.appleselectshop.repository.ProductRepository;
//import com.sparta.appleselectshop.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final FolderRepository folderRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//
//    @Transactional
//    public ProductResponseDto createProduct(ProductRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 추가 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Product product = productRepository.saveAndFlush(new Product(requestDto, user.getId()));
//
//            return new ProductResponseDto(product);
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public Page<Product> getProducts(HttpServletRequest request,
//                                     int page, int size, String sortBy, boolean isAsc) {
//        // 페이징 처리
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 조회 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            Page<Product> products;
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                products = productRepository.findAllByUserId(user.getId(), pageable);
//            } else {
//                products = productRepository.findAll(pageable);
//            }
//
//            return products;
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            Product product = productRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
//                    () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//            );
//
//            product.update(requestDto);
//
//            return product.getId();
//
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional
//    public void updateBySearch(Long id, ItemDto itemDto) {
//        Product product = productRepository.findById(id).orElseThrow(
//                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
//        );
//        product.updateByItemDto(itemDto);
//    }
//
//    @Transactional
//    public Product addFolder(Long productId, Long folderId, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 1) 상품을 조회합니다.
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new NullPointerException("해당 상품 아이디가 존재하지 않습니다."));
//
//            // 2) 관심상품을 조회합니다.
//            Folder folder = folderRepository.findById(folderId)
//                    .orElseThrow(() -> new NullPointerException("해당 폴더 아이디가 존재하지 않습니다."));
//
//            // 3) 조회한 폴더와 관심상품이 모두 로그인한 회원의 소유인지 확인합니다.
//            Long loginUserId = user.getId();
//            if (!product.getUserId().equals(loginUserId) || !folder.getUser().getId().equals(loginUserId)) {
//                throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다~^^");
//            }
//
//            // 중복확인
//            Optional<Product> overlapFolder = productRepository.findByIdAndFolderList_Id(product.getId(), folder.getId());
//
//            if(overlapFolder.isPresent()) {
//                throw new IllegalArgumentException("중복된 폴더입니다.");
//            }
//
//            // 4) 상품에 폴더를 추가합니다.
//            product.addFolder(folder);
//
//            return product;
//        } else {
//            return null;
//        }
//    }
//
//}

package com.sparta.appleselectshop.service;

import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
import com.sparta.appleselectshop.dto.ProductRequestDto;
import com.sparta.appleselectshop.dto.ProductResponseDto;
import com.sparta.appleselectshop.entity.Folder;
import com.sparta.appleselectshop.entity.Product;
import com.sparta.appleselectshop.entity.User;
import com.sparta.appleselectshop.entity.UserRoleEnum;
import com.sparta.appleselectshop.naver.dto.ItemDto;
import com.sparta.appleselectshop.repository.FolderRepository;
import com.sparta.appleselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FolderRepository folderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        System.out.println("ProductService.createProduct");
        System.out.println("user.getUsername() = " + user.getUsername());

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = productRepository.saveAndFlush(new Product(requestDto, user.getId()));

        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(User user,
                                     int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> products;

        if (userRoleEnum == UserRoleEnum.USER) {
            // 사용자 권한이 USER일 경우
            products = productRepository.findAllByUserId(user.getId(), pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products;
    }

    @Transactional
    public Long updateProduct(Long id, ProductMypriceRequestDto requestDto, User user) {

        Product product = productRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
        );

        product.update(requestDto);

        return product.getId();

    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto);
    }

    @Transactional
    public Product addFolder(Long productId, Long folderId, User user) {

        // 1) 상품을 조회합니다.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("해당 상품 아이디가 존재하지 않습니다."));

        // 2) 관심상품을 조회합니다.
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new NullPointerException("해당 폴더 아이디가 존재하지 않습니다."));

        // 3) 조회한 폴더와 관심상품이 모두 로그인한 회원의 소유인지 확인합니다.
        Long loginUserId = user.getId();
        if (!product.getUserId().equals(loginUserId) || !folder.getUser().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다~^^");
        }

        // 중복확인
        Optional<Product> overlapFolder = productRepository.findByIdAndFolderList_Id(product.getId(), folder.getId());

        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        // 4) 상품에 폴더를 추가합니다.
        product.addFolder(folder);

        return product;
    }

}