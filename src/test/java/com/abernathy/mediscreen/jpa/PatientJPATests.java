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
        patientRepository.save(testPatientOne);

        Patient testPatientTwo = new Patient();
        testPatientTwo.setFamilyName("FamilyNameTwo");
        testPatientTwo.setGivenName("GivenNameTwo");
        testPatientTwo.setAddress("AddressTwo");
        testPatientTwo.setDob(new Date());
        testPatientTwo.setPhone("111-222-3333");
        patientRepository.save(testPatientTwo);

        assertNotNull(patientRepository.findById(1));
        assertNotNull(patientRepository.findById(2));
        Patient loadPatientOne = patientRepository.findById(1).get();
        Patient loadPatientTwo = patientRepository.findById(2).get();
        assertEquals(testPatientOne.getFamilyName(), loadPatientOne.getFamilyName());
        assertEquals(testPatientTwo.getFamilyName(), loadPatientTwo.getFamilyName());
    }

}
