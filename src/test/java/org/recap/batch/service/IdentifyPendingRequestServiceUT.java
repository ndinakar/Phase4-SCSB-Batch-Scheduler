package org.recap.batch.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

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
       Mockito.when(commonService.pendingRequest(scsbCircUrl, ScsbConstants.CHECK_PENDING_REQUEST_IN_DB)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(mockidentifyPendingRequestService.identifyPendingRequestService(scsbCircUrl)).thenCallRealMethod();
        String status=mockidentifyPendingRequestService.identifyPendingRequestService(scsbCircUrl);
        assertNotNull(status);
    }

}
