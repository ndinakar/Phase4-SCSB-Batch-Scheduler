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

import java.util.Date;

/**
 * Created by angelind on 17/5/17.
 */
@Service
public class AccessionService {

    private static final Logger logger = LoggerFactory.getLogger(AccessionService.class);

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * This method makes a rest call to Solr Client micro service to initiate the accession process.
     *
     * @param serverProtocol    the server protocol
     * @param solrClientUrl     the solr client url
     * @param reportCreatedDate the report created date
     * @return status of the accession process
     */
    public String processAccession(String serverProtocol, String solrClientUrl, Date reportCreatedDate) {
        String resultStatus = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<Date> httpEntity = new HttpEntity<>(reportCreatedDate, headers);
            ResponseEntity<String> responseEntity = getRestTemplate().exchange(serverProtocol + solrClientUrl + RecapConstants.ACCESSION_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (Exception e) {
            logger.error(RecapConstants.LOG_ERROR, e);
            resultStatus = e.getMessage();
        }
        return resultStatus;
    }

}
