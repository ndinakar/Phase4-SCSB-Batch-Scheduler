package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.recap.spring.SwaggerAPIProvider;

import java.util.HashMap;
import java.util.Map;

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
    CommonService commonService;

    @Mock
    PurgeEmailAddressService purgeEmailAddressService;

    @Test
    public void testPurgeEmailAddressService() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity httpEntity = new HttpEntity<>(headers);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(ScsbCommonConstants.STATUS, ScsbConstants.SUCCESS);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        ReflectionTestUtils.setField(purgeEmailAddressService,"commonService",commonService);
        Mockito.when(purgeEmailAddressService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(purgeEmailAddressService.commonService.getRestTemplate().exchange(scsbCircUrl + ScsbConstants.PURGE_EMAIL_URL, HttpMethod.GET, httpEntity, Map.class)).thenReturn(responseEntity);
        Mockito.when(purgeEmailAddressService.commonService.executePurge(scsbCircUrl, ScsbConstants.PURGE_EMAIL_URL)).thenReturn(responseEntity.getBody());
        Mockito.when(purgeEmailAddressService.purgeEmailAddress(scsbCircUrl)).thenCallRealMethod();
        resultMap = purgeEmailAddressService.purgeEmailAddress(scsbCircUrl);
        assertNotNull(resultMap);
        assertEquals(ScsbConstants.SUCCESS, resultMap.get(ScsbCommonConstants.STATUS));
    }
}
