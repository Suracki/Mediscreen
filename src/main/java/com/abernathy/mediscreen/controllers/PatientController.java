package com.abernathy.mediscreen.controllers;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    PatientService patientService;

    private static final Logger logger = LogManager.getLogger("PatientController");

    @GetMapping("/patient/add")
    public String addRuleForm(Patient patient) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addForm(patient);
    }

    @PostMapping("/patient/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/validate endpoint");
        return patientService.validate(patient,result,model);
    }

}
