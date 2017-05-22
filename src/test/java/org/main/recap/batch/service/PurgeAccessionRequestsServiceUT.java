package org.main.recap.batch.service;

import org.junit.Test;
import org.main.recap.BaseTestCase;
import org.main.recap.RecapConstants;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 22/5/17.
 */
public class PurgeAccessionRequestsServiceUT extends BaseTestCase {

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    PurgeAccessionRequestsService purgeAccessionRequestsService;

    @Test
    public void testPurgeAccessionRequestsService() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(purgeAccessionRequestsService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(purgeAccessionRequestsService.getRestTemplate().exchange(serverProtocol + scsbCircUrl + RecapConstants.PURGE_ACCESSION_REQUEST_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(purgeAccessionRequestsService.purgeAccessionRequests(serverProtocol, scsbCircUrl)).thenCallRealMethod();
        String status = purgeAccessionRequestsService.purgeAccessionRequests(serverProtocol, scsbCircUrl);
        assertNotNull(status);
        assertEquals(status, RecapConstants.SUCCESS);
    }
}
