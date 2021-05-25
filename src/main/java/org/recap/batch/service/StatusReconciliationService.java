package org.recap.batch.service;

import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class StatusReconciliationService {

    @Autowired
    private CommonService commonService;
    
    public String statusReconciliation(String scsbCoreUrl) {
        HttpEntity httpEntity = commonService.getHttpEntity();
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(scsbCoreUrl + ScsbConstants.STATUS_RECONCILIATION_URL, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }

}
