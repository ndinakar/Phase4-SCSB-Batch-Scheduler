package org.recap.batch.service;

import org.junit.Test;
import org.mockito.*;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class StatusReconciliationServiceUT extends BaseTestCaseUT {

    @InjectMocks
    StatusReconciliationService statusReconciliationService;

    @Mock
    CommonService commonService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ResponseEntity<String> responseEntity;

    @Test
    public void testStatusReconciliation() {
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange( ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),
                ArgumentMatchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        String status=statusReconciliationService.statusReconciliation("scsbCoreUrl");
        assertEquals(ScsbConstants.SUCCESS,status);
    }
}
