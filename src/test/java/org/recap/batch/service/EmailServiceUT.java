package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.EmailPayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class EmailServiceUT extends BaseTestCase {

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    EmailService emailService;

    @Test
    public void testEmailService() {
        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(RecapCommonConstants.PURGE_EXCEPTION_REQUESTS);
        emailPayLoad.setStartDate(new Date());
        emailPayLoad.setStatus(RecapConstants.SUCCESS);

        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<EmailPayLoad> httpEntity = new HttpEntity<>(emailPayLoad, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(emailService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(emailService.commonService.getRestTemplate().exchange(solrClientUrl + RecapConstants.BATCH_JOB_EMAIL_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(emailService.sendEmail(solrClientUrl, emailPayLoad)).thenCallRealMethod();
        String status = emailService.sendEmail(solrClientUrl, emailPayLoad);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
}
