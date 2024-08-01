package com.bloodbank.management.service;



import java.util.List;
import java.util.Optional;

import com.bloodbank.management.entity.Donor;

public interface DonorService {

    List<Donor> getAllDonors();

    Donor registerDonor(Donor donor);

    void deleteDonor(Long id);

    Optional<Donor> findById(Long id);
}
