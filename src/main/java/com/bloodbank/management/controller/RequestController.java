package com.bloodbank.management.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.management.entity.Request;
import com.bloodbank.management.service.RequestService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private RequestService requestService;

    @Autowired
    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @PostMapping
    public ResponseEntity<Request> addRequest(@RequestBody Request request) {
        Request addedRequest = requestService.addRequest(request);
        return new ResponseEntity<>(addedRequest, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok("Request deleted successfully");
    }
}

