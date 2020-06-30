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
 * Created by harikrishnanv on 22/6/17.
 */
public class SubmitCollectionServiceUT extends BaseTestCase {

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    SubmitCollectionService submitCollectionService;

    @Test
    public void submitCollectionTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(submitCollectionService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(submitCollectionService.commonService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.SUBMIT_COLLECTION_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(submitCollectionService.submitCollection(scsbCircUrl)).thenCallRealMethod();
        String status = submitCollectionService.submitCollection(scsbCircUrl);
        assertNotNull(status);
        assertEquals(RecapConstants.SUCCESS, status);
    }

}