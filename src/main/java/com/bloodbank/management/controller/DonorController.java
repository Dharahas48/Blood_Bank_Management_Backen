package com.bloodbank.management.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.management.entity.Donor;
import com.bloodbank.management.service.DonorService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    @Autowired
    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    public List<Donor> getAllDonors() {
        return donorService.getAllDonors();
    }

    @PostMapping
    public ResponseEntity<Donor> registerDonor(@RequestBody Donor donor) {
        Donor registeredDonor = donorService.registerDonor(donor);
        return new ResponseEntity<>(registeredDonor, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonor(@PathVariable("id") Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.ok("Donor deleted successfully");
    }
}
