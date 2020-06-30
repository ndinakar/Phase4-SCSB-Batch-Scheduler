package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Created by harikrishnanv on 19/6/17.
 */
@Service
public class SubmitCollectionService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to scsb circ microservice to initiate the submit collection process.
     *
     * @param scsbCircUrl    the scsb circ url
     * @return status of the submit collection process.
     */
    public String submitCollection(String scsbCircUrl) {
        return commonService.executeService(scsbCircUrl,  RecapConstants.SUBMIT_COLLECTION_URL, HttpMethod.POST);
    }
}
