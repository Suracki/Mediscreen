package com.abernathy.mediscreen.jpa;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.repository.PatientRepository;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class PatientJPATests {

    @Autowired
    DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    PatientRepository patientRepository;

    @Test
    void testCreatePatientEntity() {
        int startingDbSize = patientRepository.findAll().size();

        Patient testPatient = new Patient();
        testPatient.setFamilyName("TestFam");
        testPatient.setGivenName("TestGiven");
        testPatient.setAddress("TestAddress");
        testPatient.setDob(new Date());
        testPatient.setPhone("100-222-3333");
        patientRepository.save(testPatient);

        int endingDbSize = patientRepository.findAll().size();

        assertEquals(endingDbSize, startingDbSize + 1);
    }

    @Test
    void testGetPatientEntity() {
        Patient testPatientOne = new Patient();
        testPatientOne.setFamilyName("FamilyNameOne");
        testPatientOne.setGivenName("GivenNameOne");
        testPatientOne.setAddress("AddressOne");
        testPatientOne.setDob(new Date());
        testPatientOne.setPhone("111-222-3333");
        int idOne = patientRepository.save(testPatientOne).getPatientId();

        Patient testPatientTwo = new Patient();
        testPatientTwo.setFamilyName("FamilyNameTwo");
        testPatientTwo.setGivenName("GivenNameTwo");
        testPatientTwo.setAddress("AddressTwo");
        testPatientTwo.setDob(new Date());
        testPatientTwo.setPhone("111-222-3333");
        int idTwo = patientRepository.save(testPatientTwo).getPatientId();

        assertNotNull(patientRepository.findById(idOne));
        assertNotNull(patientRepository.findById(idTwo));
        Patient loadPatientOne = patientRepository.findById(idOne).get();
        Patient loadPatientTwo = patientRepository.findById(idTwo).get();
        assertEquals(testPatientOne.getFamilyName(), loadPatientOne.getFamilyName());
        assertEquals(testPatientTwo.getFamilyName(), loadPatientTwo.getFamilyName());
    }

    @Test
    void testUpdatePatientEntity() {
        //Add a patient to the database and get name from database for later comparison
        Patient testPatientOne = new Patient();
        testPatientOne.setFamilyName("FamilyNameOne");
        testPatientOne.setGivenName("GivenNameOne");
        testPatientOne.setAddress("AddressOne");
        testPatientOne.setDob(new Date());
        testPatientOne.setPhone("111-222-3333");
        int patientId = patientRepository.save(testPatientOne).getPatientId();

        String loadFamilyNameBeforeUpdate = patientRepository.findById(patientId).get().getFamilyName();
        String loadGivenNameBeforeUpdate = patientRepository.findById(patientId).get().getGivenName();

        //Update the patient in the database and get the updated names for comparison
        Patient testPatientTwo = new Patient();
        testPatientTwo.setId(patientId);
        testPatientTwo.setFamilyName("FamilyNameOne");
        testPatientTwo.setGivenName("GivenNameTwo");
        testPatientTwo.setAddress("AddressTwo");
        testPatientTwo.setDob(new Date());
        testPatientTwo.setPhone("111-222-3333");

        patientRepository.save(testPatientTwo);
        String loadFamilyNameAfterUpdate = patientRepository.findById(patientId).get().getFamilyName();
        String loadGivenNameAfterUpdate = patientRepository.findById(patientId).get().getGivenName();

        //Assert that first name has changed, last name has not
        assertEquals(loadFamilyNameBeforeUpdate, loadFamilyNameAfterUpdate);
        assertNotEquals(loadGivenNameBeforeUpdate, loadGivenNameAfterUpdate);
    }

}
