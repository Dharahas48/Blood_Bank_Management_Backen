package com.bloodbank.management.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.management.entity.Donor;



public interface DonorRepository extends JpaRepository<Donor, Long> {

}
