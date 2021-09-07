package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping
    public ModelAndView listCustomers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/list");
        return modelAndView;
    }

}
