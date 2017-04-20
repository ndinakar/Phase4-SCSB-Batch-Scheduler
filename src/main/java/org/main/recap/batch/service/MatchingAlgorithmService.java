package org.main.recap.batch.service;

import org.main.recap.RecapConstants;
import org.main.recap.model.batch.SolrIndexRequest;
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
 * Created by rajeshbabuk on 3/4/17.
 */
@Service
public class MatchingAlgorithmService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmService.class);

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public String initiateMatchingAlgorithm(String serverProtocol, String solrClientUrl) {
        String resultStatus = null;
        try {
            SolrIndexRequest solrIndexRequest = getSolrIndexRequest();
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);

            ResponseEntity<String> responseEntity = getRestTemplate().exchange(serverProtocol + solrClientUrl + RecapConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ex);
            resultStatus = ex.getMessage();
        }
        return resultStatus;
    }

    public SolrIndexRequest getSolrIndexRequest() {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(RecapConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        solrIndexRequest.setCreatedDate(new Date());
        return solrIndexRequest;
    }
}
