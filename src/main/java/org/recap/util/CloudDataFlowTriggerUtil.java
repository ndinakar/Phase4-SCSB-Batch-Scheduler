package org.recap.util;

import lombok.extern.slf4j.Slf4j;
import org.recap.batch.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dinakar N created on 08/03/24
 */
@Service
@Slf4j
public class CloudDataFlowTriggerUtil {

    @Value("${scdf.url}")
    private String scdfURL;


    private CommonService commonService;

    public CloudDataFlowTriggerUtil(){
        this.commonService = new CommonService();
    }

    public String launchTask(String taskName) {
        log.info("taskName from DB {}", taskName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);
        //HttpEntity httpEntity = commonService.getHttpEntity();
        String path = "http://dataflow-server:9393" + "/tasks/executions/launch?name="+taskName;
        log.info("URI {}", path);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(path, HttpMethod.POST,httpEntity,String.class);
        log.info("Task {} lanched successfully and response: {}",taskName,responseEntity.toString());
        return responseEntity.getBody();
    }
}
