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

/**
 * Created by akulak on 19/5/17.
 */
@Service
public class AccessionReconcilationService {

    private static final Logger logger = LoggerFactory.getLogger(DailyReconcilationService.class);

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * This method makes a rest call to Scsb Circ micro service to initiate the accession reconciliation process.
     *
     * @param serverProtocol the server protocol
     * @param scsbCircUrl    the scsb circ url
     * @return status of the accession reconciliation process
     */
    public String accessionReconcilation(String serverProtocol, String scsbCircUrl) {
        String resultStatus = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<JobEntity> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = getRestTemplate().exchange(serverProtocol + scsbCircUrl +RecapConstants.ACCESSION_RECOCILATION_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (RestClientException e) {
            logger.error(RecapConstants.LOG_ERROR,e);
            resultStatus = e.getMessage();
        }
        return resultStatus;
    }
}
