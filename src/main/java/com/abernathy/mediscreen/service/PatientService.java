package com.abernathy.mediscreen.service;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    private PatientRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Value("${docker.assessment.url}")
    private String urlAsmt;
    @Value("${docker.history.url}")
    private String urlNote;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    //Methods to serve Front End requests

    /**
     * Method to populate Model for frontend
     * Obtains all patients from repository and adds to model
     * Then returns redirect to list url
     *
     * @param model Model object to hold data loaded from repo
     * @return redirect url String
     */
    public String home(Model model)
    {
        model.addAttribute("patients", repository.findAll());
        model.addAttribute("urlAsmt", urlAsmt);
        model.addAttribute("urlNote", urlNote);
        return "patient/list";
    }

    /**
     * Method to get redirect for form to add a new patient
     *
     * @param patient Patient to be added
     * @return url String
     */
    public String addForm(Patient patient) {
        return "patient/add";
    }

    /**
     * Method to validate provided Patient
     * Adds Patient to repository if valid & updates model
     * Returns to form if any errors found
     *
     * @param patient Patient object to be added
     * @param result BindingResult for validation
     * @param model Model object
     * @return url String
     */
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        if (!result.hasErrors()){
            repository.save(patient);
            model.addAttribute("patients", repository.findAll());
            model.addAttribute("urlAsmt", urlAsmt);
            model.addAttribute("urlNote", urlNote);
            return "redirect:/patient/list";
        }
        return "patient/add";
    }

    /**
     * Method to populate View Patient page
     * Obtains Patient with specific ID from repository and adds to model
     * Then returns redirect to view url
     *
     * @param id id parameter of Patient
     * @param model Model object to hold data loaded from repo
     * @return url String
     */
    public String view(Integer id, Model model) {
        Patient patient = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("currentPatient", patient);
        model.addAttribute("urlNote", urlNote);
        return "patient/view";
    }

    /**
     * Method to get redirect for form to update existing Patient
     * Verifies that privided ID does match an element in the repo
     * Then returns url to update form
     *
     * @param id Patient's ID value
     * @param model Model object
     * @return url string
     */
    public String showUpdateForm(Integer id, Model model) {
        Patient patient = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + id));
        model.addAttribute("patient", patient);
        model.addAttribute("urlNote", urlNote);
        return "patient/update";
    }

    /**
     * Method to validate provided Patient
     * Updates existing element in repo if valid & updates model
     * Returns to update form if not valid
     *
     * @param id Patient's ID value
     * @param patient Patient with updated fields
     * @param result BindingResult for validation
     * @param model Model object
     * @return url string
     */
    public String update(Integer id, Patient patient,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/update";
        }

        patient.setId(id);
        repository.save(patient);
        model.addAttribute("patients", repository.findAll());
        model.addAttribute("urlAsmt", urlAsmt);
        model.addAttribute("urlNote", urlNote);
        return "redirect:/patient/list";
    }

    //Methods to serve REST API requests

    /**
     * Method to generate ResponseEntity for Patient get requests received via API
     *
     * @param id id parameter of patient
     * @return url String
     */
    public ResponseEntity<String> getFromApi(Integer id) {
        try {
            Patient patient = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
            return new ResponseEntity<String>(patient.toString(), new HttpHeaders(), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<String>("Id " + id + " not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Method to validate provided Patient received via API post request
     * Adds Patient to repository if valid & updates model
     *
     * @param patient Patient object to be added
     * @param result BindingResult for validation
     * @return ResponseEntity JSON of added element and 201 if valid, 400 if invalid
     */
    public ResponseEntity<String> addFromApi(Patient patient, BindingResult result) {
        if (!result.hasErrors()){
            repository.save(patient);
            return new ResponseEntity<String>(patient.toString(), new HttpHeaders(), HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Failed to add new entry", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to validate provided Patient received via put request
     * Updates existing element in repo if valid & updates model
     *
     * @param patient Patient with updated fields
     * @param result BindingResult for validation
     * @return ResponseEntity JSON of updated patient and 200 if valid,
     *         ResponseEntity JSON of requested update and 400 if invalid,
     *         ResponseEntity JSON of requested update and 404 if ID not found in database,
     */
    public ResponseEntity<String> updateFromApi(Patient patient,
                                                BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<String>(patient.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        try {
            repository.findById(patient.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + patient.getId()));
        }
        catch (IllegalArgumentException error) {
            return new ResponseEntity<String>(patient.toString(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        repository.save(patient);
        return new ResponseEntity<String>(patient.toString(), new HttpHeaders(), HttpStatus.OK);
    }

    //Methods to serve RETROFIT API requests

    /**
     * Method to obtain Patient for get requests received via other application services
     *
     * @param id id parameter of patient
     * @return DomainElement
     */
    public Patient getFromRetro(Integer id) {
        try {
            Patient patient = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
            return patient;
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Method to obtain Name & ID of all patients as needed by other application services
     *
     * @return Map of Id to GivenName_FamilyName
     */
    public Map<Integer, String> getPatientIndexFromRetro() {
        try {
            List<Integer> patientIds = repository.getAllPatientIds();
            List<String> patientNames = repository.getAllPatientNames();

            if (patientNames.size() != patientIds.size()) {
                throw new IllegalStateException("Database returned invalid patient data. IDs: " + patientIds.size() + " Names: " + patientNames.size());
            }

            Map<Integer, String> index = new HashMap<>();

            //For each index, get the corresponding first & last name, and create new PatientIndexElement with this
            for (int i = 0; i < patientIds.size(); i++) {
                index.put(patientIds.get(i), patientNames.get(i).replace(",", " "));
            }

            return index;
        }
        catch (IllegalStateException e) {
            //Number of indexes did not match number of first & last names
            logger.error("Error: " + e);
            return null;
        }
    }

}
