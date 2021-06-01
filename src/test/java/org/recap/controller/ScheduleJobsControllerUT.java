package org.recap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.ScheduleJobRequest;
import org.recap.model.ScheduleJobResponse;
import org.recap.quartz.SchedulerService;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class ScheduleJobsControllerUT extends BaseTestCaseUT {

    @Mock
    ScheduleJobsController scheduleJobsController;

    @Mock
    SchedulerService schedulerService;

    @InjectMocks
    ScheduleJobsController mockscheduleJobsController;

    @Mock
    ScheduleJobRequest scheduleJobRequest;

    @Test
    public void testScheduleJob() throws Exception {
        ScheduleJobRequest scheduleJobRequest = getScheduleJobRequest();
        Mockito.when(schedulerService.scheduleJob(Mockito.anyString(),Mockito.anyString())).thenReturn(ScsbConstants.JOB_SUCCESS_SCHEDULING);
        ScheduleJobResponse scheduleJobResponse =mockscheduleJobsController.scheduleJob(scheduleJobRequest);
        assertNotNull(scheduleJobResponse);
        assertEquals(ScsbConstants.JOB_SUCCESS_SCHEDULING,scheduleJobResponse.getMessage());
    }

    private ScheduleJobRequest getScheduleJobRequest() {
        ScheduleJobRequest scheduleJobRequest=new ScheduleJobRequest();
        scheduleJobRequest.setScheduleType(ScsbConstants.SCHEDULE);
        scheduleJobRequest.setJobName(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS);
        scheduleJobRequest.setCronExpression("0/10 * * * * ? *");
        scheduleJobRequest.setJobId(1);
        assertNotNull(scheduleJobRequest.getScheduleType());
        assertNotNull(scheduleJobRequest.getJobId());
        assertNotNull(scheduleJobRequest.getJobName());
        assertNotNull(scheduleJobRequest.getCronExpression());
        return scheduleJobRequest;
    }

    @Test
    public void testScheduleJob_reschedule() throws Exception {
        Mockito.when(schedulerService.rescheduleJob(Mockito.anyString(),Mockito.anyString())).thenReturn(ScsbConstants.JOB_SUCCESS_RESCHEDULING);
        Mockito.when(scheduleJobRequest.getScheduleType()).thenReturn(ScsbConstants.RESCHEDULE);
        Mockito.when(scheduleJobRequest.getJobName()).thenReturn(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS);
        Mockito.when(scheduleJobRequest.getCronExpression()).thenReturn("0/10 * * * * ? *");
        ScheduleJobResponse scheduleJobResponse =mockscheduleJobsController.scheduleJob(scheduleJobRequest);
        assertNotNull(scheduleJobResponse);
        assertEquals(ScsbConstants.JOB_SUCCESS_RESCHEDULING,scheduleJobResponse.getMessage());
    }

    @Test
    public void testScheduleJob_unschedule() throws Exception {
        Mockito.when(schedulerService.unscheduleJob(Mockito.anyString())).thenReturn(ScsbConstants.JOB_SUCCESS_UNSCHEDULING);
        Mockito.when(scheduleJobRequest.getScheduleType()).thenReturn(ScsbConstants.UNSCHEDULE);
        Mockito.when(scheduleJobRequest.getJobName()).thenReturn(ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS);
        Mockito.when(scheduleJobRequest.getCronExpression()).thenReturn("0/10 * * * * ? *");
        ScheduleJobResponse scheduleJobResponse =mockscheduleJobsController.scheduleJob(scheduleJobRequest);
        assertNotNull(scheduleJobResponse);
        assertEquals(ScsbConstants.JOB_SUCCESS_UNSCHEDULING,scheduleJobResponse.getMessage());
    }

    @Test
    public void testScheduleJob_Exception() {
        Mockito.when(scheduleJobRequest.getScheduleType()).thenReturn(ScsbConstants.SCHEDULE);
        Mockito.when(schedulerService.scheduleJob(null,null)).thenThrow(NullPointerException.class);
        ScheduleJobResponse scheduleJobResponse=mockscheduleJobsController.scheduleJob(scheduleJobRequest);
        assertNull(scheduleJobResponse.getMessage());
    }
}
