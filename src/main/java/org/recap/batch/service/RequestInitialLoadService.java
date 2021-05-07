package org.recap.batch.service;

import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Created by hemalathas on 16/6/17.
 */
@Service
public class RequestInitialLoadService {

    @Autowired
    protected CommonService commonService;

    public String requestInitialLoad(String scsbCircUrl) {
        return commonService.executeService(scsbCircUrl,  ScsbConstants.REQUEST_DATA_LOAD_URL, HttpMethod.POST);
    }
}
