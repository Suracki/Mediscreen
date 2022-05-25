package com.abernathy.mediscreen.api;


import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.repository.PatientRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PatientControllerUITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private static PatientRepository patientRepository;

    @Test
    public void patientControllerGetListEndpoint() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/patient/list").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertTrue(mvcResult.getResponse().getStatus() == 200);
    }

    @Test
    public void patientControllerGetAddPatientForm() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/patient/add").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertTrue(mvcResult.getResponse().getStatus() == 200);
    }

    @Test
    public void patientControllerPostValidateAddsEntry() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                post("/patient/validate")
                        .param("familyName", "testname")
                        .param("givenName", "testname")
                        .param("dob","2015-12-31T00:00:00.000Z")
                        .param("sex", "M")
                        .param("address", "testaddress")
                        .param("phone", "111-222-3333")
                        .accept(MediaType.ALL)).andReturn();

        //Verify entry is added to DB and we are redirected (302)
        assertTrue(mvcResult.getResponse().getStatus() == 302);
        Mockito.verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
    }

    @Test
    public void bidListControllerPostValidateDoesNotAddBadEntry() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                post("/patient/validate")
                        .param("familyName", "testname")
                        .param("givenName", "testname")
                        .param("dob","2015-12-31T00:00:00.000Z")
                        .param("sex", "M")
                        .param("address", "testaddress")
                        .param("phone", "telephone")
                        .accept(MediaType.ALL)).andReturn();

        //Verify no entres are added to DB and we remain on add page (200)
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        Mockito.verify(patientRepository, Mockito.times(0)).save(any(Patient.class));
    }


}
