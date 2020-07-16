package org.recap.batch.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest
public class IdentifyPendingRequestServiceUT {

    @Mock
    CommonService commonService;

    @Value("${scsb.circ.url}")
    protected String scsbCircUrl;

    @Mock
    IdentifyPendingRequestService mockidentifyPendingRequestService;

    @Test
    public void testidentifyPendingRequestService() throws Exception {
        ReflectionTestUtils.setField(mockidentifyPendingRequestService,"commonService",commonService);
       Mockito.when(commonService.pendingRequest(scsbCircUrl,RecapConstants.CHECK_PENDING_REQUEST_IN_DB)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(mockidentifyPendingRequestService.identifyPendingRequestService(scsbCircUrl)).thenCallRealMethod();
        String status=mockidentifyPendingRequestService.identifyPendingRequestService(scsbCircUrl);
        assertNotNull(status);
    }

}
