package org.recap.util;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCase;
import org.recap.ScsbConstants;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.job.JobParamDataDto;
import org.recap.model.job.JobParamDto;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by rajeshbabuk on 3/7/17.
 */
public class JobDataParameterUtilUT extends BaseTestCase {

    @InjectMocks
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    ScsbJobService scsbJobService;

    @Test
    public void buildJobRequestParameterMap() throws Exception {
        String jobName = ScsbConstants.GENERATE_ACCESSION_REPORT_JOB;
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setJobName(jobName);
        JobParamDataDto jobParamDataDto = new JobParamDataDto();
        jobParamDataDto.setParamName(ScsbConstants.FETCH_TYPE);
        jobParamDataDto.setParamValue("1");
        jobParamDto.setJobParamDataDtos(Arrays.asList(jobParamDataDto));

        when(scsbJobService.getJobParamsByJobName(jobName)).thenReturn(jobParamDto);
        Map<String, String> parameterMap = jobDataParameterUtil.buildJobRequestParameterMap(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        assertNotNull(parameterMap);
        assertTrue(parameterMap.containsKey(ScsbConstants.FETCH_TYPE));
        assertEquals("1",parameterMap.get(ScsbConstants.FETCH_TYPE));
    }

    @Test
    public void buildJobRequestParameterMapTest() throws Exception {
        String jobName = ScsbConstants.GENERATE_ACCESSION_REPORT_JOB;
        JobParamDto jobParamDto = new JobParamDto();
        when(scsbJobService.getJobParamsByJobName(jobName)).thenReturn(jobParamDto);
        Map<String, String> parameterMap = jobDataParameterUtil.buildJobRequestParameterMap(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        assertNotNull(parameterMap);
    }

    @Test
    public void getDateFormatStringForExport() throws Exception {
        String dateFormatStringForExport = jobDataParameterUtil.getDateFormatStringForExport(new Date());
        assertNotNull(dateFormatStringForExport);
    }

    @Test
    public void getFromDate() throws Exception {
        Date fromDate = jobDataParameterUtil.getFromDate(new Date());
        assertNotNull(fromDate);
    }
}
