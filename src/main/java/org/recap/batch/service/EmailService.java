package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.model.EmailPayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
@Service
public class EmailService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to solr client microservice to send an email with the job execution information.
     *
     * @param solrClientUrl  the solr client url
     * @param emailPayLoad   the email pay load
     * @return status of sending email
     */
    public String sendEmail(String solrClientUrl, EmailPayLoad emailPayLoad) {
        HttpHeaders headers = commonService.getHttpHeaders();
        HttpEntity<EmailPayLoad> httpEntity = new HttpEntity<>(emailPayLoad, headers);
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.BATCH_JOB_EMAIL_URL, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
