package org.recap.util;

import lombok.extern.slf4j.Slf4j;
import org.recap.batch.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dinakar N created on 08/03/24
 */
@Service
@Slf4j
public class CloudDataFlowTriggerUtil {

    @Value("scdf.url")
    private String scdfURL;


    private CommonService commonService;

    public CloudDataFlowTriggerUtil(){
        this.commonService = new CommonService();
    }

    public String launchTask(String taskName) {
        log.info("taskName from DB {}", taskName);
        HttpEntity httpEntity = commonService.getHttpEntity();
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(scdfURL + "/tasks/executions/launch?name="+taskName, HttpMethod.POST,httpEntity,String.class);
        log.info("Task {} lanched successfully and response: {}",taskName,responseEntity.toString());
        return responseEntity.getBody();
    }
}
