package com.daxuepai.gaoxiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(@RequestParam("name") String name,
                       @RequestParam("age") String age,
                       ModelAndView modelAndView){
        modelAndView.addObject("hello", "hello  world");
        modelAndView.addObject("name", name);
        modelAndView.addObject("age", age);
        return "test";
    }
}
