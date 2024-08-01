package com.bloodbank.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.management.entity.Request;





public interface RequestRepository extends JpaRepository<Request, Long> {
}

