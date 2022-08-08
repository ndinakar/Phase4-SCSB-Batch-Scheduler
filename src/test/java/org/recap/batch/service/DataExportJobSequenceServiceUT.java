package org.recap.batch.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbConstants;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataExportJobSequenceServiceUT extends BaseTestCaseUT {

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    String scsbEtlUrl;

    @Mock
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

        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        try {
            Map<String, String> requestParameterMap = new HashMap<>();
            requestParameterMap.put(ScsbConstants.DATE, String.valueOf(createdDate));
            ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
            Mockito.doNothing().when(commonService).setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
            Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),
                    ArgumentMatchers.any(HttpMethod.class),
                    ArgumentMatchers.any(),
                    ArgumentMatchers.<Class<String>>any())).thenReturn(responseEntity);
            Mockito.when(dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl, createdDate, exportStringDate)).thenCallRealMethod();
            String response=dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl, createdDate, exportStringDate);
            assertEquals(responseEntity.getBody(), response);
        }catch (Exception e){
            e.printStackTrace();;
        }
    }

    @Test
    public void dataExportTriggerJobTest(){
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
            Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),
                    ArgumentMatchers.any(HttpMethod.class),
                    ArgumentMatchers.any(),
                    ArgumentMatchers.<Class<String>>any())).thenReturn(responseEntity);
            Mockito.when(dataExportJobSequenceService.dataExportTriggerJob(scsbEtlUrl)).thenCallRealMethod();
            String response=dataExportJobSequenceService.dataExportTriggerJob(scsbEtlUrl);
            assertEquals(responseEntity.getBody(), response);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
