package com.abernathy.mediscreen.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger("PatientController");

    @GetMapping("/")
    public String getHomepage(Model model) {
        logger.info("User connected to / endpoint");
        return "home";
    }

}
