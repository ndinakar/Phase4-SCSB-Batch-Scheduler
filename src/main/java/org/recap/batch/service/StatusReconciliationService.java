package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class StatusReconciliationService {

    private static final Logger logger = LoggerFactory.getLogger(StatusReconciliationService.class);

    @Autowired
    private CommonService commonService;

    
    public String statusReconcilation(String scsbCircUrl) {
        HttpHeaders headers = commonService.getHttpHeaders();
        HttpEntity<JobEntity> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.STATUS_RECOCILATION_URL, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }

}
