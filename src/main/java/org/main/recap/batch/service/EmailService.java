package org.main.recap.batch.service;

import org.main.recap.RecapConstants;
import org.main.recap.model.EmailPayLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public String sendEmail(String serverProtocol, String solrClientUrl, EmailPayLoad emailPayLoad) {
        String resultStatus = null;
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity<EmailPayLoad> httpEntity = new HttpEntity<>(emailPayLoad, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(serverProtocol + solrClientUrl + RecapConstants.BATCH_JOB_EMAIL_URL, HttpMethod.POST, httpEntity, String.class);
            resultStatus = responseEntity.getBody();
            return resultStatus;
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ex);
            return resultStatus;
        }
    }
}
