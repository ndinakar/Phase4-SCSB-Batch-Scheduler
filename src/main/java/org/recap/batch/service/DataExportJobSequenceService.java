package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.util.JobDataParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeshbabuk on 10/7/17.
 */
@Service
public class DataExportJobSequenceService {

    private static final Logger logger = LoggerFactory.getLogger(DataExportJobSequenceService.class);

    @Autowired
    JobDataParameterUtil jobDataParameterUtil;

    @Autowired
    private CommonService commonService;

    /**
     * This method makes a rest call to scsb etl microservice to initiate the process of incremental and delete data export.
     *
     * @param scsbEtlUrl the scsb etl url
     * @return status of incremental and delete data export.
     */
    public String dataExportJobSequence(String scsbEtlUrl, Date createdDate, String exportStringDate) {
        HttpEntity httpEntity = commonService.getHttpEntity();
        Map<String, String> requestParameterMap = new HashMap<>();
        commonService.setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(scsbEtlUrl + RecapConstants.DATA_EXPORT_JOB_SEQUENCE_URL, HttpMethod.GET, httpEntity, String.class, requestParameterMap);
        return responseEntity.getBody();
    }
}
