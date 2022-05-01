package de.othr.sw.pumpal.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

    @RequestMapping("/")
    public String start() {
        return "index";
    }
}
