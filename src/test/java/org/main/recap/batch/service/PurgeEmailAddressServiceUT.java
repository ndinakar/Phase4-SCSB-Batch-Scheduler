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
 * Created by rajeshbabuk on 19/4/17.
 */
public class PurgeEmailAddressServiceUT extends BaseTestCase {

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    PurgeEmailAddressService purgeEmailAddressService;

    @Test
    public void testPurgeEmailAddressService() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(purgeEmailAddressService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(purgeEmailAddressService.getRestTemplate().exchange(scsbCircUrl + RecapConstants.PURGE_EMAIL_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(purgeEmailAddressService.purgeEmailAddress(scsbCircUrl)).thenCallRealMethod();
        String status = purgeEmailAddressService.purgeEmailAddress(scsbCircUrl);
        assertNotNull(status);
        assertEquals(status, RecapConstants.SUCCESS);
    }
}
