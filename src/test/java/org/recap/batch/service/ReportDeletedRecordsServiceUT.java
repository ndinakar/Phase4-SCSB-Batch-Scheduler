package org.recap.batch.service;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.recap.repository.jpa.JobDetailsRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReportDeletedRecordsServiceUT extends BaseTestCaseUT {

    @InjectMocks
    ReportDeletedRecordsService reportDeletedRecordsService;

    @Mock
    CommonService commonService;

    @Mock
    ResponseEntity<String> responseEntity;

    @Mock
    RestTemplate restTemplate;

    @Mock
    JobDetailsRepository jobDetailsRepository;

    @Test
    public void testreportDeletedRecords() {
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange("scsbCoreUrl" + ScsbConstants.REPORT_DELETED_RECORDS_URL, HttpMethod.GET, null, String.class)).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        String status=reportDeletedRecordsService.reportDeletedRecords("scsbCoreUrl");
        assertEquals(ScsbConstants.SUCCESS,status);
    }

    @Test
    public void testgetJobDetailsRepository() {
        JobDetailsRepository jobDetailsRepository=reportDeletedRecordsService.getJobDetailsRepository();
        assertNotNull(jobDetailsRepository);
    }
}
