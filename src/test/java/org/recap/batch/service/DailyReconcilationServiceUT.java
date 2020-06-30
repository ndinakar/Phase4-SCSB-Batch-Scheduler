package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
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
 * Created by akulak on 10/5/17.
 */
public class DailyReconcilationServiceUT extends BaseTestCase {

    @Value("${scsb.circ.url}")
    String solrCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    private DailyReconcilationService dailyReconcilationService;

    @Test
    public void testDailyReconcilationService() throws Exception{
        String jobName  = "DailyReconcilation";
        Date lastExecutedTime = new Date();
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(dailyReconcilationService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(dailyReconcilationService.commonService.getRestTemplate().exchange(solrCircUrl +RecapConstants.DAILY_RECONCILATION_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(dailyReconcilationService.dailyReconcilation(solrCircUrl)).thenCallRealMethod();
        String status = dailyReconcilationService.dailyReconcilation(solrCircUrl);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }
}
