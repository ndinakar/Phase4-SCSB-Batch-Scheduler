package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentifyPendingRequestService {

    @Autowired
    private CommonService commonService;

    
    /**
     * Check pending msges in queue string.
     *
     * @param scsbCircUrl the scsb circ url
     * @return the string
     */
    public String identifyPendingRequestService(String scsbCircUrl) {
        return commonService.pendingRequest(scsbCircUrl, RecapConstants.CHECK_PENDING_REQUEST_IN_DB);
    }
}
