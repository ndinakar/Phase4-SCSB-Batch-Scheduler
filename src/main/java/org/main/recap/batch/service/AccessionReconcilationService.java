package org.main.recap.batch.service;

import org.main.recap.RecapConstants;
import org.main.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Created by akulak on 19/5/17.
 */
@Service
public class AccessionReconcilationService {

    private static final Logger logger = LoggerFactory.getLogger(DailyReconcilationService.class);

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public String accessionReconcilation(String serverProtocol, String solrCircUrl, String jobName, Date createdDate) {
        String resultStatus = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<JobEntity> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = getRestTemplate().exchange(serverProtocol + solrCircUrl +RecapConstants.ACCESSION_RECOCILATION_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (RestClientException e) {
            logger.error(RecapConstants.LOG_ERROR,e);
            resultStatus = e.getMessage();
        }
        return resultStatus;
    }
}
