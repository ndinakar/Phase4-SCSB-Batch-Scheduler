package org.recap.batch.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbConstants;
import org.recap.model.job.JobDto;
import org.recap.model.job.JobParamDataDto;
import org.recap.model.job.JobParamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 24/May/2021
 */
public class ScsbJobServiceUT extends BaseTestCaseUT {

    @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}")
    String solrClientUrl;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ScsbJobService scsbJobService;

    @Test
    public void testGetAllJobs() {
        JobDto jobDto = new JobDto();
        jobDto.setJobName(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        jobDto.setJobDescription(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        HttpEntity httpEntity = new HttpEntity(new HttpHeaders());
        Mockito.when(scsbJobService.getHttpEntity()).thenReturn(httpEntity);
        Mockito.when(scsbJobService.getRestTemplate()).thenReturn(restTemplate);
        ResponseEntity<List<JobDto>> responseEntity = new ResponseEntity<>(Collections.singletonList(jobDto), HttpStatus.OK);
        Mockito.when(scsbJobService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.GET_ALL_JOBS_URL, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<JobDto>>() {})).thenReturn(responseEntity);
        Mockito.when(scsbJobService.getAllJobs()).thenCallRealMethod();
        List<JobDto> jobs = scsbJobService.getAllJobs();
        assertNotNull(jobs);
    }

    @Test
    public void testGetJobByName() {
        String jobName = ScsbConstants.GENERATE_ACCESSION_REPORT_JOB;
        JobDto jobDto = new JobDto();
        jobDto.setJobName(jobName);
        jobDto.setJobDescription(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(ScsbConstants.JOB_NAME, jobName);
        HttpEntity httpEntity = scsbJobService.getHttpEntity();
        Mockito.when(scsbJobService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(scsbJobService.getRestTemplate()).thenReturn(restTemplate);
        ResponseEntity<JobDto> responseEntity = new ResponseEntity<>(jobDto, HttpStatus.OK);
        Mockito.when(scsbJobService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.GET_JOB_BY_NAME_URL, HttpMethod.GET, httpEntity, JobDto.class, parameterMap)).thenReturn(responseEntity);
        Mockito.when(scsbJobService.getJobByName(jobName)).thenCallRealMethod();
        JobDto job = scsbJobService.getJobByName(jobName);
        assertNotNull(job);
    }

    @Test
    public void testGetJobParamsByJobName() {
        String jobName = ScsbConstants.GENERATE_ACCESSION_REPORT_JOB;
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setJobName(jobName);
        JobParamDataDto jobParamDataDto = new JobParamDataDto();
        jobParamDataDto.setParamName(ScsbConstants.FETCH_TYPE);
        jobParamDataDto.setParamValue("1");
        jobParamDto.setJobParamDataDtos(Arrays.asList(jobParamDataDto));
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(ScsbConstants.JOB_NAME, jobName);
        HttpEntity httpEntity = scsbJobService.getHttpEntity();
        Mockito.when(scsbJobService.getHttpEntity()).thenCallRealMethod();
        Mockito.when(scsbJobService.getRestTemplate()).thenReturn(restTemplate);
        ResponseEntity<JobParamDto> responseEntity = new ResponseEntity<>(jobParamDto, HttpStatus.OK);
        Mockito.when(scsbJobService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.GET_JOB_PARAMS_BY_JOB_NAME_URL, HttpMethod.GET, httpEntity, JobParamDto.class, parameterMap)).thenReturn(responseEntity);
        Mockito.when(scsbJobService.getJobParamsByJobName(jobName)).thenCallRealMethod();
        JobParamDto jobParam = scsbJobService.getJobParamsByJobName(jobName);
        assertNotNull(jobParam);
    }

    @Test
    public void testUpdateJob() {
        JobDto jobDto = new JobDto();
        jobDto.setJobName(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        HttpEntity<JobDto> httpEntity = new HttpEntity<>(jobDto, scsbJobService.getHttpHeaders());
        Mockito.when(scsbJobService.getHttpHeaders()).thenCallRealMethod();
        Mockito.when(scsbJobService.getRestTemplate()).thenReturn(restTemplate);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ScsbConstants.SUCCESS, HttpStatus.OK);
        Mockito.when(scsbJobService.getRestTemplate().exchange(solrClientUrl + ScsbConstants.UPDATE_JOB_URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(responseEntity);
        Mockito.when(scsbJobService.updateJob(jobDto)).thenCallRealMethod();
        String status = scsbJobService.updateJob(jobDto);
        assertNotNull(status);
    }
}
