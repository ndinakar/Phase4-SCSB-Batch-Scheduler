package org.main.recap.batch.service;

import org.main.recap.util.JobDataParameterUtil;
import org.main.recap.RecapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

/**
 * Created by rajeshbabuk on 23/6/17.
 */
@Service
public class IncrementalExportPulService {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportPulService.class);

    @Value("${data.dump.email.pul.to}")
    private String dataDumpEmailPulTo;

    @Autowired
    JobDataParameterUtil jobDataParameterUtil;

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * This method makes a rest call to scsb etl microservice to initiate the process of incremental export for PUL.
     *
     * @param scsbEtlUrl the scsb etl url
     * @return status of incremental export for PUL
     */
    public String incrementalExportPul(String scsbEtlUrl, String jobName, Date createdDate) {
        String resultStatus = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(RecapConstants.API_KEY, RecapConstants.RECAP);
            HttpEntity httpEntity = new HttpEntity<>(headers);
            Map<String, String> requestParameterMap = jobDataParameterUtil.buildJobRequestParameterMap(jobName);
            requestParameterMap.put(RecapConstants.DATE, jobDataParameterUtil.getDateFormatStringForExport(createdDate));
            requestParameterMap.put(RecapConstants.EMAIL_TO_ADDRESS, dataDumpEmailPulTo);

            ResponseEntity<String> responseEntity = getRestTemplate().exchange(scsbEtlUrl + RecapConstants.DATA_EXPORT_ETL_URL, HttpMethod.GET, httpEntity, String.class, requestParameterMap);
            resultStatus = responseEntity.getBody();
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ex);
            resultStatus = ex.getMessage();
        }
        return resultStatus;
    }
}
