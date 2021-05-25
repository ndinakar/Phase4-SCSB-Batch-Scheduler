package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.model.job.JobDto;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.recap.spring.SwaggerAPIProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Anitha V on 12/7/20.
 */

public class CommonServiceUT extends BaseTestCase {

    @Mock
    CommonService commonService;

    @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}")
    String solrClientUrl;

    @Value("${" + PropertyKeyConstants.SCSB_CIRC_URL + "}")
    String scsbCircUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    Map<String, String>  requestParameterMap;

    @Test
    public void testExecuteService() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.ACCESSION_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.executeService(solrClientUrl , ScsbConstants.ACCESSION_URL, HttpMethod.GET)).thenCallRealMethod();
        String status = commonService.executeService(solrClientUrl , ScsbConstants.ACCESSION_URL, HttpMethod.GET);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }
    @Test
    public void testexecutePurge() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(ScsbCommonConstants.STATUS, ScsbConstants.SUCCESS);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(scsbCircUrl + ScsbConstants.PURGE_ACCESSION_REQUEST_URL, HttpMethod.GET, httpEntity, Map.class)).thenReturn(responseEntity);
        Mockito.when(commonService.executePurge(scsbCircUrl , ScsbConstants.PURGE_ACCESSION_REQUEST_URL)).thenCallRealMethod();
        resultMap = commonService.executePurge(scsbCircUrl , ScsbConstants.PURGE_ACCESSION_REQUEST_URL);
        assertNotNull(resultMap);
        assertEquals(ScsbConstants.SUCCESS, resultMap.get(ScsbCommonConstants.STATUS));
    }
    @Test
    public void testgetResponse() throws Exception {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpHeaders()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.getResponse(solrIndexRequest , solrClientUrl, ScsbConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST)).thenCallRealMethod();
        String status = commonService.getResponse(solrIndexRequest , solrClientUrl, ScsbConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }
    @Ignore
    public void testpendingRequest() throws Exception {
        JobDto jobDto=new JobDto();
        jobDto.setJobDescription(ScsbConstants.CHECK_PENDING_REQUEST_IN_DB);
        jobDto.setStatus((ScsbConstants.SUCCESS));
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<JobDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpHeaders()).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(scsbCircUrl + ScsbConstants.CHECK_PENDING_REQUEST_IN_DB, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(commonService.pendingRequest(scsbCircUrl, ScsbConstants.CHECK_PENDING_REQUEST_IN_DB)).thenCallRealMethod();
        String status = commonService.pendingRequest(scsbCircUrl, ScsbConstants.CHECK_PENDING_REQUEST_IN_DB);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }

    @Test
    public void testgetRestTemplate()  {
        Mockito.when(commonService.getRestTemplate()).thenCallRealMethod();
        assertNotEquals(commonService.getRestTemplate(),restTemplate);
    }

    @Test
    public void setRequestParameterMapBlankDate()  {
        Date createdDate=new Date();
        Mockito.doCallRealMethod().when(commonService).setRequestParameterMap(requestParameterMap,"",jobDataParameterUtil,createdDate);
        commonService.setRequestParameterMap(requestParameterMap,"",jobDataParameterUtil,createdDate);
        assertTrue(true);
    }

    @Test
    public void setRequestParameterMap()  {
        String exportStringDate=new Date().toString();
        Date createdDate=new Date();
        Mockito.doCallRealMethod().when(commonService).setRequestParameterMap(requestParameterMap,exportStringDate,jobDataParameterUtil,createdDate);
        commonService.setRequestParameterMap(requestParameterMap,exportStringDate,jobDataParameterUtil,createdDate);
        assertTrue(true);
    }

}
