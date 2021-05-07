package org.recap.util;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCase;
import org.recap.ScsbConstants;
import org.recap.model.jpa.JobParamDataEntity;
import org.recap.model.jpa.JobParamEntity;
import org.recap.repository.jpa.JobParamDetailRepository;

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
    JobParamDetailRepository jobParamDetailRepository;

    @Test
    public void buildJobRequestParameterMap() throws Exception {
        String jobName = ScsbConstants.GENERATE_ACCESSION_REPORT_JOB;
        JobParamEntity jobParamEntity =  new JobParamEntity();
        JobParamDataEntity jobParamDataEntity = new JobParamDataEntity();
        jobParamDataEntity.setParamName(ScsbConstants.FETCH_TYPE);
        jobParamDataEntity.setParamValue("1");
        jobParamEntity.setJobParamDataEntities(Arrays.asList(jobParamDataEntity));

        when(jobParamDetailRepository.findByJobName(jobName)).thenReturn(jobParamEntity);
        Map<String, String> parameterMap = jobDataParameterUtil.buildJobRequestParameterMap(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        assertNotNull(parameterMap);
        assertTrue(parameterMap.containsKey(ScsbConstants.FETCH_TYPE));
        assertEquals("1",parameterMap.get(ScsbConstants.FETCH_TYPE));
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
