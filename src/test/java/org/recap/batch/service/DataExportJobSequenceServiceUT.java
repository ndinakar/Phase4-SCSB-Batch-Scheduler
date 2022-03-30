package org.recap.batch.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
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

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({org.springframework.web.client.RestTemplate.class,DataExportJobSequenceService.class})
public class DataExportJobSequenceServiceUT extends BaseTestCase  {

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    private String scsbEtlUrl;


    DataExportJobSequenceService dataExportJobSequenceService;

    @Mock
    CommonService commonService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    RestTemplate tem;
    @Before
    public void setUp() throws Exception {
        dataExportJobSequenceService=new DataExportJobSequenceService();
       // tem=PowerMockito.mock(RestTemplate.class);
       // PowerMockito.whenNew(RestTemplate.class).withNoArguments().thenReturn(tem);
    }


    @Test
    public void testdataExportJobSequenceService() throws Exception {

        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= new Date(System.currentTimeMillis()).toString();
        Map<String, String> requestParameterMap= new HashMap<>();
        requestParameterMap.put(ScsbConstants.DATE, String.valueOf(createdDate));
        ReflectionTestUtils.setField(dataExportJobSequenceService,"commonService",commonService);
        ReflectionTestUtils.setField(dataExportJobSequenceService,"jobDataParameterUtil",jobDataParameterUtil);
        HttpEntity httpEntity = commonService.getHttpEntity();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.doNothing().when(commonService).setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
       // PowerMockito.when(tem.exchange(scsbEtlUrl + ScsbConstants.DATA_EXPORT_JOB_SEQUENCE_URL, HttpMethod.GET, httpEntity, String.class, requestParameterMap)).thenReturn(responseEntity);
     //   PowerMockito.verifyNew(RestTemplate.class).withNoArguments();
        Mockito.when(restTemplate.exchange( ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),
                ArgumentMatchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl,createdDate,exportStringDate)).thenCallRealMethod();
        String status= dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl,createdDate,exportStringDate);
    }
}
