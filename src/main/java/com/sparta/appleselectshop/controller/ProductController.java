////package com.sparta.appleselectshop.controller;
////
////import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
////import com.sparta.appleselectshop.dto.ProductRequestDto;
////import com.sparta.appleselectshop.dto.ProductResponseDto;
////import com.sparta.appleselectshop.service.ProductService;
////import org.springframework.web.bind.annotation.*;
////
////import java.sql.SQLException;
////import java.util.List;
////
////@RestController
////@RequestMapping("/api")
////public class ProductController {
////
////    // 관심 상품 등록하기
////    @PostMapping("/products")
////    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
////        ProductService productService = new ProductService();
////        // 응답 보내기
////        return productService.createProduct(requestDto);
////    }
////
////    // 관심 상품 조회하기
////    @GetMapping("/products")
////    public List<ProductResponseDto> getProducts() throws SQLException {
////        ProductService productService = new ProductService();
////        // 응답 보내기
////        return productService.getProducts();
////    }
////
////    // 관심 상품 최저가 등록하기
////    @PutMapping("/products/{id}")
////    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
////        ProductService productService = new ProductService();
////        // 응답 보내기 (업데이트된 상품 id)
////        return productService.updateProduct(id, requestDto);
////    }
////
////}
//
////package com.sparta.appleselectshop.controller;
////
////import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
////import com.sparta.appleselectshop.dto.ProductRequestDto;
////import com.sparta.appleselectshop.dto.ProductResponseDto;
////import com.sparta.appleselectshop.service.ProductService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.web.bind.annotation.*;
////
////import java.sql.SQLException;
////import java.util.List;
////
////@RestController
////@RequestMapping("/api")
////@RequiredArgsConstructor
////public class ProductController {
////
////    private final ProductService productService;
////
////    // 관심 상품 등록하기
////    @PostMapping("/products")
////    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
////        // 응답 보내기
////        return productService.createProduct(requestDto);
////    }
////
////    // 관심 상품 조회하기
////    @GetMapping("/products")
////    public List<ProductResponseDto> getProducts() throws SQLException {
////        // 응답 보내기
////        return productService.getProducts();
////    }
////
////    // 관심 상품 최저가 등록하기
////    @PutMapping("/products/{id}")
////    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
////        // 응답 보내기 (업데이트된 상품 id)
////        return productService.updateProduct(id, requestDto);
////    }
////
////}
//
//package com.sparta.appleselectshop.controller;
//
//import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
//import com.sparta.appleselectshop.dto.ProductRequestDto;
//import com.sparta.appleselectshop.dto.ProductResponseDto;
//import com.sparta.appleselectshop.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ProductService productService;
//
//    // 관심 상품 등록하기
//    @PostMapping("/products")
//    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
//        // 응답 보내기
//        return productService.createProduct(requestDto);
//    }
//
//    // 관심 상품 조회하기
//    @GetMapping("/products")
//    public List<ProductResponseDto> getProducts() {
//        // 응답 보내기
//        return productService.getProducts();
//    }
//
//    // 관심 상품 최저가 등록하기
//    @PutMapping("/products/{id}")
//    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
//        // 응답 보내기 (업데이트된 상품 id)
//        return productService.updateProduct(id, requestDto);
//    }
//
//}

package com.sparta.appleselectshop.controller;

import com.sparta.appleselectshop.dto.ProductMypriceRequestDto;
import com.sparta.appleselectshop.dto.ProductRequestDto;
import com.sparta.appleselectshop.dto.ProductResponseDto;
import com.sparta.appleselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // DB insert (관심 상품)
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, HttpServletRequest request) {
        // 응답 보내기
        return productService.createProduct(requestDto, request);
    }

    // DB select
    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(HttpServletRequest request) {
        // 응답 보내기
        return productService.getProducts(request);
    }

    // DB update (최저가 가격 업데이트)
    @PutMapping("/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto, HttpServletRequest request) {    // path variable + json
        // 응답 보내기 (업데이트된 상품 id)
        return productService.updateProduct(id, requestDto, request);
    }
}