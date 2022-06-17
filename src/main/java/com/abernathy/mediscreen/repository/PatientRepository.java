package com.abernathy.mediscreen.repository;

import com.abernathy.mediscreen.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("SELECT patientId FROM Patient ORDER BY patientId")
    List<Integer> getAllPatientIds();

    @Query("SELECT givenName, familyName FROM Patient ORDER BY patientId")
    List<String> getAllPatientNames();
}
