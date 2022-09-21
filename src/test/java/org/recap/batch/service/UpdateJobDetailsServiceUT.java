package org.recap.batch.service;

import org.junit.Test;
import org.mockito.*;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.job.JobDto;
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
    ScsbJobService scsbJobService;

    @Mock
    JobDto jobDto;

    @InjectMocks
    UpdateJobDetailsService updateJobDetailsService;

    @Mock
    CommonService commonService;

    @Mock
    ResponseEntity<String> responseEntity;


    @Test
    public void testUpdateJobDetailsService() throws Exception {
        Mockito.when(scsbJobService.getJobByName(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS)).thenReturn(jobDto);
        Mockito.when(jobDto.getCronExpression()).thenReturn("* * * * * ? *");
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange( ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),
                ArgumentMatchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(scsbJobService.updateJob(Mockito.any())).thenReturn(ScsbConstants.SUCCESS);
        String status = updateJobDetailsService.updateJob("solrClientUrl", ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS, new Date(), 1l);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }

    @Test
    public void testUpdateJobDetailsServiceTest() throws Exception {
        JobDto jobDto = new JobDto();
        Mockito.when(scsbJobService.getJobByName(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS)).thenReturn(jobDto);
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange( ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),
                ArgumentMatchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(scsbJobService.updateJob(Mockito.any())).thenReturn(ScsbConstants.SUCCESS);
        String status = updateJobDetailsService.updateJob("solrClientUrl", ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS, new Date(), 1l);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }
}
