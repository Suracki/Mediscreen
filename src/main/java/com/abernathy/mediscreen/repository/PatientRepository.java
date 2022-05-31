package com.abernathy.mediscreen.repository;

import com.abernathy.mediscreen.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
