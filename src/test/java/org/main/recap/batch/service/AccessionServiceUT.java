package org.main.recap.batch.service;

import org.junit.Test;
import org.main.recap.BaseTestCase;
import org.main.recap.RecapConstants;
import org.main.recap.model.batch.SolrIndexRequest;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by angelind on 29/5/17.
 */
public class AccessionServiceUT extends BaseTestCase{

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    AccessionService accessionService;

    @Test
    public void processAccession() throws Exception {
        Date createdDate = new Date();
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
        HttpEntity<Date> httpEntity = new HttpEntity<>(createdDate, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(accessionService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(accessionService.getRestTemplate().exchange(solrClientUrl + RecapConstants.ACCESSION_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(accessionService.processAccession(solrClientUrl)).thenCallRealMethod();
        String status = accessionService.processAccession(solrClientUrl);
        assertNotNull(status);
        assertEquals(status, RecapConstants.SUCCESS);
    }

}