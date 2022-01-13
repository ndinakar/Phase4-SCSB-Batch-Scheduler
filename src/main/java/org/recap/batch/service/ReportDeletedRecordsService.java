package org.recap.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by rajeshbabuk on 12/4/17.
 */

@Slf4j
@Service
public class ReportDeletedRecordsService {

    @Autowired
    private CommonService commonService;

    /**
     * This method makes a rest call to scsb-circ microservice to report about the deleted records, in important transaction tables in SCSB.
     *
     * @param scsbCoreUrl
     * @return
     */
    public String reportDeletedRecords(String scsbCoreUrl){
        HttpEntity httpEntity = commonService.getHttpEntity();
        log.info("{},{}", scsbCoreUrl , ScsbConstants.REPORT_DELETED_RECORDS_URL);
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(scsbCoreUrl + ScsbConstants.REPORT_DELETED_RECORDS_URL, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
