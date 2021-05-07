package org.recap.batch.service;

import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by angelind on 14/9/17.
 */
@Service
public class CheckAndNotifyPendingRequestService {

    @Autowired
    private CommonService commonService;


    /**
     * Check pending msges in queue string.
     *
     * @param scsbCircUrl the scsb circ url
     * @return the string
     */
    public String checkPendingMsgesInQueue(String scsbCircUrl) {
        return commonService.pendingRequest(scsbCircUrl, ScsbConstants.NOTIFY_IF_PENDING_REQUEST);
    }

}
