package com.abernathy.mediscreen.service;

import com.abernathy.mediscreen.domain.Patient;
import com.abernathy.mediscreen.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService extends BaseService<Patient> {

        public PatientService(PatientRepository patientRepository) {
            super (patientRepository);
        }

}
