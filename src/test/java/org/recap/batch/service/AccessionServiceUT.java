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

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by angelind on 29/5/17.
 */
public class AccessionServiceUT extends BaseTestCase{

    @Value("${scsb.solr.doc.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    private AccessionService accessionService;

    @Mock
    CommonService commonService;

    @Test
    public void processAccession() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        ReflectionTestUtils.setField(accessionService,"commonService",commonService);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(accessionService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(accessionService.commonService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.ACCESSION_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(accessionService.commonService.executeService(solrClientUrl , ScsbConstants.ACCESSION_URL, HttpMethod.GET)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(accessionService.processAccession(solrClientUrl)).thenCallRealMethod();
        String status = accessionService.processAccession(solrClientUrl);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }

}