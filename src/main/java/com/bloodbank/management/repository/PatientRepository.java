package com.bloodbank.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.management.entity.Patient;





public interface PatientRepository extends JpaRepository<Patient, Long> {
}
