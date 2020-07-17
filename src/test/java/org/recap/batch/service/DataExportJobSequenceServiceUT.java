package org.recap.batch.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({org.springframework.web.client.RestTemplate.class,DataExportJobSequenceService.class})
public class DataExportJobSequenceServiceUT extends BaseTestCase  {

    @Value("${scsb.etl.url}")
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


    @Ignore
    public void testdataExportJobSequenceService() throws Exception {

        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        Map<String, String> requestParameterMap= new HashMap<>();
        requestParameterMap.put(RecapConstants.DATE, String.valueOf(createdDate));
        ReflectionTestUtils.setField(dataExportJobSequenceService,"commonService",commonService);
        ReflectionTestUtils.setField(dataExportJobSequenceService,"jobDataParameterUtil",jobDataParameterUtil);
        HttpEntity httpEntity = commonService.getHttpEntity();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RecapConstants.SUCCESS, HttpStatus.OK);
        Mockito.doNothing().when(commonService).setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
       // PowerMockito.when(tem.exchange(scsbEtlUrl + RecapConstants.DATA_EXPORT_JOB_SEQUENCE_URL, HttpMethod.GET, httpEntity, String.class, requestParameterMap)).thenReturn(responseEntity);
     //   PowerMockito.verifyNew(RestTemplate.class).withNoArguments();
        Mockito.when(restTemplate.exchange( Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>> any(),
                Matchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl,createdDate,exportStringDate)).thenCallRealMethod();
        String status= dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl,createdDate,exportStringDate);
    }
}
