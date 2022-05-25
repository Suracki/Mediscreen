package com.abernathy.mediscreen.service;

import com.abernathy.mediscreen.domain.DomainElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public abstract class BaseService <E extends DomainElement> {

    private JpaRepository<E, Integer> repository;

    public BaseService(JpaRepository<E, Integer> repository) {
        this.repository = repository;
    }


    private String getType() {
        String className = getClass().getSimpleName().replace("Service","");
        return className.substring(0,1).toLowerCase() + className.substring(1);
    }

    //Methods to serve Front End requests

    /**
     * Method to populate Model for frontend
     * Obtains all elements of this type from repository and adds to model
     * Then returns redirect to list url
     *
     * @param model Model object to hold data loaded from repo
     * @return redirect url String
     */
    public String home(Model model)
    {
        model.addAttribute(getType() + "s", repository.findAll());
        return getType() + "/list";
    }

    /**
     * Method to get redirect for form to add a new element
     *
     * @param e DomainElement object of type to be added
     * @return url String
     */
    public String addForm(DomainElement e) {
        return getType() + "/add";
    }

    /**
     * Method to validate provided DomainElement
     * Adds DomainElement to repository if valid & updates model
     * Returns to form if any errors found
     *
     * @param e DomainElement object to be added
     * @param result BindingResult for validation
     * @param model Model object
     * @return url String
     */
    public String validate(@Valid E e, BindingResult result, Model model) {
        if (!result.hasErrors()){
            repository.save(e);
            model.addAttribute(getType() + "s", repository.findAll());
            return "redirect:/" + getType() + "/list";
        }
        return getType() + "/add";
    }

    public String view(Integer id, Model model) {
        E e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid " + getType() + " Id:" + id));
        model.addAttribute("currentPatient", e);
        return getType() + "/view";
    }

    //Methods to serve REST API requests

    public ResponseEntity<String> get(Integer id, Model model) {
        try {
            E e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid " + getType() + " Id:" + id));
            return new ResponseEntity<String>(e.toString(), new HttpHeaders(), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<String>("Id " + id + " not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> addPatient(E e, BindingResult result, Model model) {
        if (!result.hasErrors()){
            repository.save(e);
            model.addAttribute(getType() + "s", repository.findAll());
            return new ResponseEntity<String>(e.toString(), new HttpHeaders(), HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Failed to add new entry.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
