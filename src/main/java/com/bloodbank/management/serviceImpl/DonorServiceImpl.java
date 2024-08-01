package com.bloodbank.management.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.management.entity.Donor;
import com.bloodbank.management.repository.DonorRepository;
import com.bloodbank.management.securityImpl.ResourceNotFoundException;
import com.bloodbank.management.service.DonorService;

import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;

    @Autowired
    public DonorServiceImpl(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Override
    public Donor registerDonor(Donor donor) {
        return donorRepository.save(donor);
    }

    @Override
    public void deleteDonor(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        donorRepository.delete(donor);
    }

    @Override
    public Optional<Donor> findById(Long id) {
        return donorRepository.findById(id);
    }
}