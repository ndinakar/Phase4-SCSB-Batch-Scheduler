package org.recap.batch.service;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class CheckAndNotifyPendingRequestServiceUT extends BaseTestCaseUT {

    @InjectMocks
    CheckAndNotifyPendingRequestService checkAndNotifyPendingRequestService;

    @Mock
    CommonService commonService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ResponseEntity<String> responseEntity;


    @Test
    public void testcheckPendingMsgesInQueue() {
        Mockito.when(commonService.pendingRequest("scsbCircUrl", ScsbConstants.NOTIFY_IF_PENDING_REQUEST)).thenCallRealMethod();
        Mockito.when(commonService.getRestTemplate()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange( Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>> any(),
                Matchers.<Class<String>> any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(ScsbConstants.SUCCESS);
        String status=checkAndNotifyPendingRequestService.checkPendingMsgesInQueue("scsbCircUrl");
        assertEquals(ScsbConstants.SUCCESS,status);

    }
}
