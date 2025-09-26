package org.example.creditprocessing.service;

import dto.clientProcessing.ClientProductDto;

public interface CreditDecisionService {
    boolean isApproved(Long clientId, ClientProductDto desiredProduct);
}


