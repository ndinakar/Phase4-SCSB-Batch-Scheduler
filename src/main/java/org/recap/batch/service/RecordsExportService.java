package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
public class RecordsExportService {

    @Value("${data.dump.email.cul.to}")
    private String dataDumpEmailCulTo;

    @Value("${data.dump.email.nypl.to}")
    private String dataDumpEmailNyplTo;

    @Value("${data.dump.email.pul.to}")
    private String dataDumpEmailPulTo;

    @Autowired
    JobDataParameterUtil jobDataParameterUtil;
    
    @Autowired
    private CommonService commonService;

    
    /**
     * This method makes a rest call to scsb etl microservice to initiate the process of deleted records export for Columbia.
     *
     * @param scsbEtlUrl    the scsb etl url
     * @return status of deleted records export for CUL
     */
    public String exportRecords(String scsbEtlUrl, String jobName, Date createdDate, String exportStringDate, String exportInstitution) {
        HttpEntity httpEntity = commonService.getHttpEntity();
        Map<String, String> requestParameterMap = jobDataParameterUtil.buildJobRequestParameterMap(jobName);
        if (exportInstitution.equalsIgnoreCase("CUL")) {
            requestParameterMap.put(RecapConstants.EMAIL_TO_ADDRESS, dataDumpEmailCulTo);
        }
        else if (exportInstitution.equalsIgnoreCase("PUL")) {
            requestParameterMap.put(RecapConstants.EMAIL_TO_ADDRESS, dataDumpEmailPulTo);
        }
        else if (exportInstitution.equalsIgnoreCase("NYPL")) {
            requestParameterMap.put(RecapConstants.EMAIL_TO_ADDRESS, dataDumpEmailNyplTo);
        }
        commonService.setRequestParameterMap(requestParameterMap, exportStringDate, jobDataParameterUtil, createdDate);
        ResponseEntity<String> responseEntity = commonService.getRestTemplate().exchange(scsbEtlUrl + RecapConstants.DATA_EXPORT_ETL_URL, HttpMethod.GET, httpEntity, String.class, requestParameterMap);
        return responseEntity.getBody();
    }
}
