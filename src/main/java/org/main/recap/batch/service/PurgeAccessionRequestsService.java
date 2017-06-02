package org.main.recap.batch.service;

import org.main.recap.RecapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rajeshbabuk on 22/5/17.
 */
@Service
public class PurgeAccessionRequestsService {

    private static final Logger logger = LoggerFactory.getLogger(PurgeAccessionRequestsService.class);

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * This method makes a rest call to Scsb Circ micro service to initiate the process of purging accession requests.
     *
     * @param serverProtocol the server protocol
     * @param scsbCircUrl    the scsb circ url
     * @return status of purging accession requests process
     */
    public String purgeAccessionRequests(String serverProtocol, String scsbCircUrl) {
        String resultStatus = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = getRestTemplate().exchange(serverProtocol + scsbCircUrl + RecapConstants.PURGE_ACCESSION_REQUEST_URL, HttpMethod.GET, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ex);
            resultStatus = ex.getMessage();
        }
        return resultStatus;
    }
}
