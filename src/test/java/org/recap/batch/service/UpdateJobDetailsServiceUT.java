package org.recap.batch.service;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.jpa.JobEntity;
import org.recap.repository.jpa.JobDetailsRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class UpdateJobDetailsServiceUT extends BaseTestCaseUT {

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDetailsRepository jobDetailsRepository;

    @Mock
    JobEntity jobEntity;

    @InjectMocks
    UpdateJobDetailsService updateJobDetailsService;

    @Mock
    CommonService commonService;

    @Mock
    ResponseEntity<String> responseEntity;


    @Test
    public void testUpdateJobDetailsService() throws Exception {
        Mockito.when(jobDetailsRepository.findByJobName(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS)).thenReturn(jobEntity);
        Mockito.when(jobEntity.getCronExpression()).thenReturn("* * * * * ? *");
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange( Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>> any(),
                Matchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        String status = updateJobDetailsService.updateJob("solrClientUrl", ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS, new Date(), 1l);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }
}
