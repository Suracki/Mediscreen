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

    /**
     * Mapping for GET
     *
     * Serves list patients page for Mediscreen app
     *
     * @param model Model
     * @return list notes homepage
     */
    @RequestMapping("/patient/list")
    public String home(Model model)
    {
        logger.info("User connected to /patient/list endpoint");
        return patientService.home(model);
    }

    /**
     * Mapping for GET
     *
     * Serves add note page for Mediscreen app
     *
     * @param patient patient object
     * @return add patient form page
     */
    @GetMapping("/patient/add")
    public String addPatient(Patient patient) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addForm(patient);
    }

    /**
     * Mapping for POST
     *
     * Requests validation of Patient created via Add page
     *
     * @param patient patient note object
     * @param result BindingResult for validation
     * @param model Model
     * @return patient list page if successful, add patient page if unsuccessful
     */
    @PostMapping("/patient/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        logger.info("User connected to /patient/validate endpoint");
        return patientService.validate(patient,result,model);
    }

    /**
     * Mapping for GET
     *
     * Serves view patient page for Mediscreen app
     *
     * @param id patient id
     * @param model Model
     * @return view patient page
     */
    @GetMapping("/patient/view/{id}")
    public String viewPatient(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/view endpoint with id " + id);
        return patientService.view(id, model);
    }

    /**
     * Mapping for GET
     *
     * Serves update patient page for Mediscreen app
     *
     * @param id patient id
     * @param model Model
     * @return update patient page
     */
    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /patient/update/ GET endpoint for patient with id " + id);
        return patientService.showUpdateForm(id, model);
    }

    /**
     * Mapping for POST
     *
     * Requests validation of Patient updated via Update page
     *
     * @param id patient id
     * @param patient patient object
     * @param result BindingResult for validation
     * @param model Model
     * @return patient list page if successful, update patient page if unsuccessful
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @Valid Patient patient,
                                 BindingResult result, Model model) {
        logger.info("User connected to /ruleName/update/ POST endpoint for patient with id " + id);
        return patientService.update(id, patient, result, model);
    }

    //Endpoints for serving REST API

    /**
     * Mapping for POST
     *
     * Returns:
     * HttpStatus.BAD_REQUEST if note cannot be added (eg invalid data)
     * Json string & HttpStatus.CREATED if successful
     *
     * @param patient object to be added
     * @param result BindingResult for validation
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/patient/api/add")
    public ResponseEntity<String> addPatientApi(@Valid @RequestBody Patient patient, BindingResult result) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.addFromApi(patient, result);
    }

    /**
     * Mapping for GET
     *
     * Takes a Patient's ID, returns that Patient object
     *
     * Returns:
     * HttpStatus.NOT_FOUND if patient cannot be found with provided ID
     * Json string & HttpStatus.OK if successful
     *
     * @param id
     * @return Json string & HttpStatus.CREATED if successful
     */
    @GetMapping("/patient/api/get/{id}")
    public ResponseEntity<String> getPatientApi(@PathVariable("id") Integer id) {
        logger.info("User connected to /patient/get endpoint with id " + id);
        return patientService.getFromApi(id);
    }

    /**
     * Mapping for PUT
     *
     * Returns:
     * HttpStatus.NOT_FOUND if note does not exist with this ID
     * HttpStatus.BAD_REQUEST if note has errors
     * Json string & HttpStatus.OK if successful
     *
     * @param patient Patient with updated fields
     * @return Json string & HttpStatus.OK if successful
     */
    @PutMapping("/patient/api/update")
    public ResponseEntity<String> updatePatientApi(@Valid @RequestBody Patient patient, BindingResult result) {
        logger.info("User connected to /patient/add endpoint");
        return patientService.updateFromApi(patient, result);
    }

    //Endpoints for serving Retrofit calls

    /**
     * Mapping for GET
     *
     * Intended to be called by other services, returns JSON string only
     * Takes a Patient ID, returns JSON of Patient object
     *
     *
     * @param id Patient ID
     * @return Json string
     */
    @GetMapping("/patient/api/retro/get/{id}")
    @ResponseBody
    public String getPatientRetro(@PathVariable("id") Integer id) {
        logger.info("Service call made to /patient/api/retro/get/ endpoint with id " + id);
        return gson.toJson(patientService.getFromRetro(id));
    }

    /**
     * Mapping for GET
     *
     * Intended to be called by other services, returns JSON string only
     * Returns a Map of Patient IDs to Patient Names
     *
     * @return Json string
     */
    @GetMapping("/patient/api/retro/get/index")
    @ResponseBody
    public String getPatientIndex() {
        logger.info("Service call made to /patient/api/retro/get/index endpoint");
        return gson.toJson(patientService.getPatientIndexFromRetro());
    }

}
