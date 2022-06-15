package com.abernathy.mediscreen.controllers;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.service.PatientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private Gson gson = new GsonBuilder().create();

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

    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/update/ GET endpoint for patient with id " + id);
        return patientService.showUpdateForm(id, model);
    }

    @PostMapping("/patient/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid Patient patient,
                                 BindingResult result, Model model) {
        logger.info("User connected to /ruleName/update/ POST endpoint for patient with id " + id);
        return patientService.update(id, patient, result, model);
    }

    //Endpoints for serving REST API

    @PostMapping("/patient/api/add")
    public ResponseEntity<String> addPatientApi(@Valid @RequestBody Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addFromApi(patient, result, model);
    }

    @GetMapping("/patient/api/get/{id}")
    public ResponseEntity<String> getPatientApi(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/get endpoint with id " + id);
        return patientService.getFromApi(id, model);
    }

    @PutMapping("/patient/api/update")
    public ResponseEntity<String> updatePatientApi(@Valid @RequestBody Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.updateFromApi(patient, result, model);
    }

    //Endpoints for serving Retrofit calls

    @GetMapping("/patient/api/retro/get/{id}")
    @ResponseBody
    public String getPatientRetro(@PathVariable("id") Integer id) {
        logger.info("Service call made to /patient/api/retro/get/ endpoint with id " + id);
        return gson.toJson(patientService.getFromRetro(id));
    }

    @GetMapping("/patient/api/retro/get/index")
    @ResponseBody
    public String getPatientIndex() {
        logger.info("Service call made to /patient/api/retro/get/index endpoint");
        return gson.toJson(patientService.getIdsFromRetro());
    }

}
