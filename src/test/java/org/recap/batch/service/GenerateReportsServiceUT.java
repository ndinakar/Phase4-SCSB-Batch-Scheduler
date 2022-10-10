package org.recap.batch.service;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.util.JobDataParameterUtil;
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
public class GenerateReportsServiceUT extends BaseTestCase{

    @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}")
    String solrClientUrl;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    RestTemplate restTemplate;

    @Mock
     CommonService commonService;

    @Mock
    GenerateReportsService generateReportsService;

    @InjectMocks
    GenerateReportsService service;

    @Test
    public void generateReport() throws Exception {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(ScsbConstants.GENERATE_ACCESSION_REPORT);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(generateReportsService,"commonService",commonService);
        ReflectionTestUtils.setField(generateReportsService,"jobDataParameterUtil",jobDataParameterUtil);
        Mockito.when(generateReportsService.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(generateReportsService.getSolrIndexRequest(createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT)).thenReturn(solrIndexRequest);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.GENERATE_REPORT_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(jobDataParameterUtil.getFromDate(createdDate)).thenCallRealMethod();
        Mockito.when(commonService.getResponse(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.any())).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(generateReportsService.getSolrIndexRequest(createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        Mockito.when(generateReportsService.generateReport(solrClientUrl, createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        String status = generateReportsService.generateReport(solrClientUrl, createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);
    }
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        return headers;
    }

    @Test
    public void generateCgdReportTest() throws Exception{
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(ScsbConstants.GENERATE_ACCESSION_REPORT);
        Date createdDate = new Date();
        solrIndexRequest.setCreatedDate(createdDate);
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        ReflectionTestUtils.setField(service,"commonService",commonService);
        ReflectionTestUtils.setField(service,"jobDataParameterUtil",jobDataParameterUtil);
        Mockito.when(service.commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(commonService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.GENERATE_CGD_ROUND_TRIP_REPORT_URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(jobDataParameterUtil.getFromDate(createdDate)).thenCallRealMethod();
        Mockito.when(commonService.getResponse(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.any())).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(service.getSolrIndexRequest(createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        Mockito.when(service.generateReport(solrClientUrl, createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT)).thenCallRealMethod();
        String status = service.generateCgdReport(solrClientUrl, createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT);
        assertNotNull(status);
        assertEquals(ScsbConstants.SUCCESS, status);

    }

}