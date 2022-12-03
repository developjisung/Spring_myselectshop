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

package com.sparta.appleselectshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class ShopController {

    // call main html
    @GetMapping("/shop")
    public ModelAndView shop() {
        return new ModelAndView("index");
    }
}