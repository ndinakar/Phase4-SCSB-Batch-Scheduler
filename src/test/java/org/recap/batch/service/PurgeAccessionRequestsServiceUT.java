package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
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
 * Created by rajeshbabuk on 22/5/17.
 */
public class PurgeAccessionRequestsServiceUT extends BaseTestCase {

    @Value("${" + PropertyKeyConstants.SCSB_CIRC_URL + "}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    CommonService commonService;

    @Mock
    PurgeAccessionRequestsService purgeAccessionRequestsService;

    @Test
    public void testPurgeAccessionRequestsService() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity httpEntity = new HttpEntity<>(headers);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(ScsbCommonConstants.STATUS, ScsbConstants.SUCCESS);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        ReflectionTestUtils.setField(purgeAccessionRequestsService,"commonService",commonService);
        Mockito.when(purgeAccessionRequestsService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(purgeAccessionRequestsService.commonService.getRestTemplate().exchange(scsbCircUrl + ScsbConstants.PURGE_ACCESSION_REQUEST_URL, HttpMethod.GET, httpEntity, Map.class)).thenReturn(responseEntity);
        Mockito.when(purgeAccessionRequestsService.commonService.executePurge(scsbCircUrl , ScsbConstants.PURGE_ACCESSION_REQUEST_URL)).thenReturn(resultMap);

        Mockito.when(purgeAccessionRequestsService.purgeAccessionRequests(scsbCircUrl)).thenCallRealMethod();
        resultMap = purgeAccessionRequestsService.purgeAccessionRequests(scsbCircUrl);
        assertNotNull(resultMap);
        assertEquals(ScsbConstants.SUCCESS, resultMap.get(ScsbCommonConstants.STATUS));
    }
}
