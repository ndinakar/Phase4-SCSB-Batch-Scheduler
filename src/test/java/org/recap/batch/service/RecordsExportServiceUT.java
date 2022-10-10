package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.util.JobDataParameterUtil;
import org.recap.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.recap.spring.SwaggerAPIProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordsExportServiceUT  extends BaseTestCase {

    @InjectMocks
    RecordsExportService recordsExportService;

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    private String scsbEtlUrl;

    @Mock
    CommonService commonService;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    PropertyUtil propertyUtil;

    @Mock
    RestTemplate restTemplate;

    @Before
    public  void setup(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(recordsExportService,"commonService",commonService);
        ReflectionTestUtils.setField(recordsExportService,"jobDataParameterUtil",jobDataParameterUtil);
    }


    @Test
    public void testexportRecords() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ScsbCommonConstants.API_KEY, SwaggerAPIProvider.getInstance().getSwaggerApiKey());
        HttpEntity<Date> httpEntity = new HttpEntity<>(headers);
        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        Map<String, String> requestParameterMap= new HashMap<>();
        requestParameterMap.put(ScsbConstants.DATE, String.valueOf(createdDate));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(commonService.getHttpEntity()).thenCallRealMethod();
        String exportInstitution = ScsbCommonConstants.PRINCETON;
        String jobName = ScsbConstants.DELETED_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase());
        Mockito.when(jobDataParameterUtil.buildJobRequestParameterMap(jobName)).thenReturn(requestParameterMap);
        commonService.setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange(scsbEtlUrl + jobName, HttpMethod.GET, httpEntity, String.class)).thenReturn(responseEntity);
     try {
           Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate,"PUL")).thenCallRealMethod();

     }catch (Exception ex){
           ex.printStackTrace();
       }

    }
}
