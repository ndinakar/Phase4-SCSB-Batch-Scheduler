package org.recap.batch.service;

import org.recap.PropertyKeyConstants;
import org.recap.ScsbConstants;
import org.recap.model.job.JobDto;
import org.recap.model.job.JobParamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

/**
 * Created by rajeshbabuk on 23/May/2021
 */
@Service
public class ScsbJobService extends CommonService {

    @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}")
    protected String solrClientUrl;

    /**
     * Get all jobs in scsb
     * @return List of Jobs
     */
    public List<JobDto> getAllJobs() {
        ResponseEntity<List<JobDto>> responseEntity = getRestTemplate().exchange(solrClientUrl + ScsbConstants.GET_ALL_JOBS_URL, HttpMethod.GET, getHttpEntity(), new ParameterizedTypeReference<List<JobDto>>() {});
        return responseEntity.getBody();
    }

    /**
     * Get job by job name
     * @param jobName Job Name
     * @return a Job
     */
    public JobDto getJobByName(String jobName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrClientUrl + ScsbConstants.GET_JOB_BY_NAME_URL).queryParam(ScsbConstants.JOB_NAME, jobName);
        ResponseEntity<JobDto> responseEntity = getRestTemplate().exchange(builder.build().encode().toUri(), HttpMethod.GET, getHttpEntity(), JobDto.class);
        return responseEntity.getBody();
    }

    /**
     * Get Job Param object containing job parameters for the given job name
     * @param jobName Job Name
     * @return Job Param Dto
     */
    public JobParamDto getJobParamsByJobName(String jobName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrClientUrl + ScsbConstants.GET_JOB_PARAMS_BY_JOB_NAME_URL).queryParam(ScsbConstants.JOB_NAME, jobName);
        ResponseEntity<JobParamDto> responseEntity = getRestTemplate().exchange(builder.build().encode().toUri(), HttpMethod.GET, getHttpEntity(), JobParamDto.class);
        return responseEntity.getBody();
    }

    /**
     * Update Job
     * @param jobDto Job Dto
     * @return status
     */
    public String updateJob(JobDto jobDto) {
        HttpEntity<JobDto> httpEntity = new HttpEntity<>(jobDto, getHttpHeaders());
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(solrClientUrl + ScsbConstants.UPDATE_JOB_URL, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
