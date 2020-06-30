package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 25/7/17.
 */
public class RequestInitialLoadServiceUT extends BaseTestCase{

    @Value("${scsb.circ.url}")
    String solrCircUrl;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RequestInitialLoadService requestInitialLoadService;

    @Test
    public void testRequestInitialLoadService(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(requestInitialLoadService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(requestInitialLoadService.commonService.getRestTemplate().exchange(solrCircUrl +RecapConstants.REQUEST_DATA_LOAD_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(requestInitialLoadService.requestInitialLoad(solrCircUrl)).thenCallRealMethod();
        String status = requestInitialLoadService.requestInitialLoad(solrCircUrl);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }

}