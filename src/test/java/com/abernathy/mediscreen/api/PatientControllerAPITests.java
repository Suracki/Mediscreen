package com.abernathy.mediscreen.api;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PatientControllerAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private static PatientRepository patientRepository;

    @Test
    public void patientControllerAPIAddsEntry() throws Exception {

        Patient patient = new Patient();
        patient.setFamilyName("testFamilyName");
        patient.setGivenName("testFirstName");
        patient.setDob(new Date());
        patient.setSex("F");
        patient.setAddress("testAddress");
        patient.setPhone("111-222-3333");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patient);

        MvcResult mvcResult = mockMvc.perform(
                post("/patient/api/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.ALL)).andReturn();

        //Verify entry is added to DB, and we get created response (201)
        assertTrue(mvcResult.getResponse().getStatus() == 201);
        Mockito.verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
    }

    @Test
    public void patientControllerAPIWillNotAddInvalidEntry() throws Exception {

        Patient patient = new Patient();
        patient.setFamilyName("testFamilyName");
        patient.setGivenName("testFirstName");
        patient.setDob(new Date());
        patient.setSex("abcd");
        patient.setAddress("testAddress");
        patient.setPhone("phone");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patient);

        MvcResult mvcResult = mockMvc.perform(
                post("/patient/api/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.ALL)).andReturn();

        //Verify no entry is added to DB, and we get Bad Request response (400)
        assertTrue(mvcResult.getResponse().getStatus() == 400);
        Mockito.verify(patientRepository, Mockito.times(0)).save(any(Patient.class));
    }

    @Test
    public void patientControllerAPIGetsEntry() throws Exception {

        //Create mock patient
        Patient patient = new Patient();
        patient.setFamilyName("testFamilyName");
        patient.setGivenName("testFirstName");
        patient.setDob(new Date());
        patient.setSex("F");
        patient.setAddress("testAddress");
        patient.setPhone("111-222-3333");

        //If our service works and asks the repo for patient with id 1, return our mock patient
        when(patientRepository.findById(1)).thenReturn(java.util.Optional.of(patient));

        //Attempt to retrieve patient
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/patient/api/get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verify entry is retrieved from DB, and we get success response (200)
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        Mockito.verify(patientRepository, Mockito.times(1)).findById(1);

    }

    @Test
    public void patientControllerAPIDoesNotGetInvalidEntry() throws Exception {

        //Attempt to retrieve patient
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/patient/api/get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verify retrieval is attempted from DB, and we get Not Found response (404)
        assertTrue(mvcResult.getResponse().getStatus() == 404);
        Mockito.verify(patientRepository, Mockito.times(1)).findById(1);

    }

    @Test
    public void patientControllerAPIUpdatesEntry() throws Exception {

        //Create mock patient with valid data
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFamilyName("testFamilyName");
        patient.setGivenName("testFirstName");
        patient.setDob(new Date());
        patient.setSex("F");
        patient.setAddress("testAddress");
        patient.setPhone("111-222-3333");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patient);

        //If our service accepts the Patient JSON and asks the repo for patient with id 1, return a valid patient
        when(patientRepository.findById(1)).thenReturn(java.util.Optional.of(patient));

        //Attempt to update patient
        MvcResult mvcResult = mockMvc.perform(
                put("/patient/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.ALL)).andReturn();

        //Verify entry is saved to DB, and we get success response (200)
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        Mockito.verify(patientRepository, Mockito.times(1)).save(any());

    }

    @Test
    public void patientControllerAPIDoesNotUpdateWithInvalidData() throws Exception {

        //Create mock patient with valid ID but invalid/missing data
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPhone("1112223333");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patient);

        //If our service accepts the Patient JSON and asks the repo for patient with id 1, return a valid patient
        when(patientRepository.findById(1)).thenReturn(java.util.Optional.of(patient));

        //Attempt to update patient
        MvcResult mvcResult = mockMvc.perform(
                put("/patient/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.ALL)).andReturn();

        //Verify entry is not saved to DB, and we get bad request response (400)
        assertTrue(mvcResult.getResponse().getStatus() == 400);
        Mockito.verify(patientRepository, Mockito.times(0)).save(any());

    }

    @Test
    public void patientControllerAPIDoesNotUpdateWithInvalidID() throws Exception {

        //Create mock patient with valid data
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFamilyName("testFamilyName");
        patient.setGivenName("testFirstName");
        patient.setDob(new Date());
        patient.setSex("F");
        patient.setAddress("testAddress");
        patient.setPhone("111-222-3333");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patient);

        //When our service accepts the Patient JSON and asks the repo for patient with id 1
        //We want to simulate not finding a patient, so we omit patientRepository thenReturn line

        //Attempt to update patient
        MvcResult mvcResult = mockMvc.perform(
                put("/patient/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.ALL)).andReturn();

        //Verify entry is not saved to DB, and we get not found response (404)
        assertTrue(mvcResult.getResponse().getStatus() == 404);
        Mockito.verify(patientRepository, Mockito.times(0)).save(any());

    }

}
