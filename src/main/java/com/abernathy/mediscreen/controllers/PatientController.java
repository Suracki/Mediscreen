package com.abernathy.mediscreen.controllers;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    PatientService patientService;

    private static final Logger logger = LogManager.getLogger("PatientController");

    //Endpoints for serving front end
    @RequestMapping("/patient/list")
    public String home(Model model)
    {
        logger.info("User connected to /patient/list endpoint");
        return patientService.home(model);
    }

    @GetMapping("/patient/add")
    public String addPatient(Patient patient) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addForm(patient);
    }

    @PostMapping("/patient/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/validate endpoint");
        return patientService.validate(patient,result,model);
    }

    @GetMapping("/patient/view/{id}")
    public String viewPatient(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/view endpoint with id " + id);
        return patientService.view(id, model);
    }

    //Endpoints for serving REST API

    @PostMapping("/patient/api/add")
    public ResponseEntity<String> addPatientApi(@Valid @RequestBody Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addPatient(patient, result, model);
    }

    @GetMapping("/patient/api/get/{id}")
    public ResponseEntity<String> getPatientApi(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/get endpoint with id " + id);
        return patientService.get(id, model);
    }

}
