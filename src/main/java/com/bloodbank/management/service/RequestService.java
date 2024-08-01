package com.bloodbank.management.service;



import java.util.List;

import com.bloodbank.management.entity.Request;

public interface RequestService {
    List<Request> getAllRequests();

    Request addRequest(Request request);

    void deleteRequest(Long id);
}
