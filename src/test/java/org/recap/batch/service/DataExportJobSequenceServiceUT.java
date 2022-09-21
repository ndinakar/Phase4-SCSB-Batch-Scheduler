package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.spring.SwaggerAPIProvider;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DataExportJobSequenceServiceUT extends BaseTestCaseUT {

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    String scsbEtlUrl;

    @InjectMocks
    DataExportJobSequenceService dataExportJobSequenceService;

    @Mock
    CommonService commonService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    RestTemplate tem;
    @Before
    public void setUp() {
        ReflectionTestUtils.setField(dataExportJobSequenceService,"commonService",commonService);
        ReflectionTestUtils.setField(dataExportJobSequenceService,"jobDataParameterUtil",jobDataParameterUtil);
    }


    @Test
    public void testdataExportJobSequenceService() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2022-07-07";

        Map<String, String> requestParameterMap = new HashMap<>();
        requestParameterMap.put(ScsbConstants.DATE, String.valueOf(createdDate));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();

        String exportInstitution = ScsbCommonConstants.PRINCETON;
        String jobName = ScsbConstants.DATA_EXPORT_JOB_SEQUENCE_URL + StringUtils.capitalize(exportInstitution.toLowerCase());
        Mockito.when(jobDataParameterUtil.buildJobRequestParameterMap(jobName)).thenReturn(requestParameterMap);
        commonService.setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);

        Mockito.when(restTemplate.exchange(scsbEtlUrl + jobName, HttpMethod.GET, httpEntity, String.class, requestParameterMap)).thenReturn(responseEntity);
        try {
            Mockito.when(dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl, createdDate, "2022-08-24")).thenCallRealMethod();
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void dataExportTriggerJobTest(){

        try{
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        String exportInstitution = ScsbCommonConstants.PRINCETON;
        String jobName = ScsbConstants.DATA_EXPORT_TRIGGER_JOB_URL + StringUtils.capitalize(exportInstitution.toLowerCase());
        Mockito.when(restTemplate.exchange(jobName, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(dataExportJobSequenceService.dataExportTriggerJob(jobName)).thenCallRealMethod();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
