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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class PurgeExceptionRequestsServiceUT extends BaseTestCase {

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    PurgeExceptionRequestsService purgeExceptionRequestsService;

    @Test
    public void testPurgeExceptionRequestsService() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(RecapCommonConstants.STATUS, RecapConstants.SUCCESS);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        Mockito.when(purgeExceptionRequestsService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(purgeExceptionRequestsService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.PURGE_EXCEPTION_REQUEST_URL, HttpMethod.GET, httpEntity, Map.class)).thenReturn(responseEntity);
        Mockito.when(purgeExceptionRequestsService.purgeExceptionRequests(scsbCircUrl)).thenCallRealMethod();
        resultMap = purgeExceptionRequestsService.purgeExceptionRequests(scsbCircUrl);
        assertNotNull(resultMap);
        assertEquals(resultMap.get(RecapCommonConstants.STATUS), RecapConstants.SUCCESS);
    }
}
