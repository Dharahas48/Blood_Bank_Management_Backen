package com.bloodbank.management.service;

import java.util.List;
import java.util.Optional;

import com.bloodbank.management.entity.Patient;



public interface PatientService {

    Patient addPatient(Patient patient);

    List<Patient> getAllPatients();

    void deletePatient(Long id);

    Optional<Patient> findById(Long id);
}