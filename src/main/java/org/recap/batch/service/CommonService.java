package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.model.jpa.JobEntity;
import org.recap.util.JobDataParameterUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
public class CommonService {

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        return new HttpEntity<>(headers);
    }

    public String executeService(String url, String jobUrl, HttpMethod httpMethod)
    {
        HttpEntity httpEntity = getHttpEntity();
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url + jobUrl, httpMethod, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RecapCommonConstants.API_KEY, RecapCommonConstants.RECAP);
        return headers;
    }

    public  Map<String, String> executePurge(String scsbCircUrl, String url) {
        HttpEntity httpEntity = getHttpEntity();
        ResponseEntity<Map> responseEntity = getRestTemplate().exchange(scsbCircUrl + url, HttpMethod.GET, httpEntity, Map.class);
        return responseEntity.getBody();
    }

    public String pendingRequest(String url, String jobUrl) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<JobEntity> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url + jobUrl, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }
    public String  getResponse(SolrIndexRequest solrIndexRequest, String solrClientUrl, String url) {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<SolrIndexRequest> httpEntity = new HttpEntity<>(solrIndexRequest, headers);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(solrClientUrl + url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }

    protected void setRequestParameterMap(Map<String, String>  requestParameterMap, String exportStringDate,JobDataParameterUtil jobDataParameterUtil,  Date createdDate) {
        if (StringUtils.isBlank(exportStringDate)) {
            requestParameterMap.put(RecapConstants.DATE, jobDataParameterUtil.getDateFormatStringForExport(createdDate));
        } else {
            requestParameterMap.put(RecapConstants.DATE, exportStringDate);
        }
    }
}
