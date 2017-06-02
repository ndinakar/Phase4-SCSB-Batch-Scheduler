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
 * Created by angelind on 2/5/17.
 */
@Service
public class GenerateReportsService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateReportsService.class);

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public String generateReport(String solrClientUrl, Date reportCreatedDate, String jobName) {
        String resultStatus = null;
        try {
            SolrIndexRequest solrIndexRequest = getSolrIndexRequest(reportCreatedDate, jobName);
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);

            ResponseEntity<String> responseEntity = getRestTemplate().exchange(solrClientUrl + RecapConstants.GENERATE_REPORT_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
        } catch (Exception e) {
            logger.error(RecapConstants.LOG_ERROR, e);
            resultStatus = e.getMessage();
        }
        return resultStatus;
    }

    public SolrIndexRequest getSolrIndexRequest(Date reportCreatedDate, String reportType) {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(reportType);
        solrIndexRequest.setCreatedDate(reportCreatedDate);
        return solrIndexRequest;
    }


}
