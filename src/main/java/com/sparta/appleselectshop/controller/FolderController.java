//package com.sparta.appleselectshop.controller;
//
//import com.sparta.appleselectshop.dto.FolderRequestDto;
//import com.sparta.appleselectshop.entity.Folder;
//import com.sparta.appleselectshop.service.FolderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class FolderController {
//
//    private final FolderService folderService;
//
//    @PostMapping("/folders")
//    public List<Folder> addFolders(
//            @RequestBody FolderRequestDto folderRequestDto,
//            HttpServletRequest request
//    ) {
//
//        List<String> folderNames = folderRequestDto.getFolderNames();
//
//        return folderService.addFolders(folderNames, request);
//    }
//}

//package com.sparta.appleselectshop.controller;
//
//import com.sparta.appleselectshop.dto.FolderRequestDto;
//import com.sparta.appleselectshop.entity.Folder;
//import com.sparta.appleselectshop.entity.Product;
//import com.sparta.appleselectshop.service.FolderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class FolderController {
//
//    private final FolderService folderService;
//
//    @PostMapping("/folders")
//    public List<Folder> addFolders(
//            @RequestBody FolderRequestDto folderRequestDto,
//            HttpServletRequest request
//    ) {
//
//        List<String> folderNames = folderRequestDto.getFolderNames();
//
//        return folderService.addFolders(folderNames, request);
//    }
//
//    // 회원이 등록한 모든 폴더 조회
//    @GetMapping("/folders")
//    public List<Folder> getFolders(
//            HttpServletRequest request
//    ) {
//        return folderService.getFolders(request);
//    }
//
//    // 회원이 등록한 폴더 내 모든 상품 조회
//    @GetMapping("/folders/{folderId}/products")
//    public Page<Product> getProductsInFolder(
//            @PathVariable Long folderId,
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String sortBy,
//            @RequestParam boolean isAsc,
//            HttpServletRequest request
//    ) {
//        return folderService.getProductsInFolder(
//                folderId,
//                page-1,
//                size,
//                sortBy,
//                isAsc,
//                request
//        );
//    }
//
//}

package com.sparta.appleselectshop.controller;

import com.sparta.appleselectshop.dto.FolderRequestDto;
import com.sparta.appleselectshop.entity.Folder;
import com.sparta.appleselectshop.entity.Product;
import com.sparta.appleselectshop.security.UserDetailsImpl;
import com.sparta.appleselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folders")
    public List<Folder> addFolders(
            @RequestBody FolderRequestDto folderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        List<String> folderNames = folderRequestDto.getFolderNames();

        System.out.println("======================================================");
        System.out.println("user.getUsername() = " + userDetails.getUsername());
        System.out.println("user.getUser() = " + userDetails.getUser());
        System.out.println("user.getUser().getPassword() = " + userDetails.getUser().getPassword());
        System.out.println("user.getUser().getId() = " + userDetails.getUser().getId());
        System.out.println("======================================================");

        return folderService.addFolders(folderNames, userDetails.getUsername());
    }

    // 회원이 등록한 모든 폴더 조회
    @GetMapping("/folders")
    public List<Folder> getFolders(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return folderService.getFolders(userDetails.getUser());
    }

    // 회원이 등록한 폴더 내 모든 상품 조회
    @GetMapping("/folders/{folderId}/products")
    public Page<Product> getProductsInFolder(
            @PathVariable Long folderId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return folderService.getProductsInFolder(
                folderId,
                page-1,
                size,
                sortBy,
                isAsc,
                userDetails.getUser()
        );
    }
}