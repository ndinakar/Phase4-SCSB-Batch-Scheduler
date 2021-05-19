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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 25/7/17.
 */
public class RequestInitialLoadServiceUT extends BaseTestCase{

    @Value("${" + PropertyKeyConstants.SCSB_CIRC_URL + "}")
    String solrCircUrl;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RequestInitialLoadService requestInitialLoadService;

    @Mock
    CommonService commonService;

    @Test
    public void testRequestInitialLoadService(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(requestInitialLoadService,"commonService",commonService);
        Mockito.when(requestInitialLoadService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(requestInitialLoadService.commonService.getRestTemplate().exchange(solrCircUrl + ScsbConstants.REQUEST_DATA_LOAD_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(requestInitialLoadService.commonService.executeService(solrCircUrl,  ScsbConstants.REQUEST_DATA_LOAD_URL, HttpMethod.POST)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(requestInitialLoadService.requestInitialLoad(solrCircUrl)).thenCallRealMethod();
        String status = requestInitialLoadService.requestInitialLoad(solrCircUrl);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }

}