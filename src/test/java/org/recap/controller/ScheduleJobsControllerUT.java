package org.recap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.ScheduleJobRequest;
import org.recap.model.ScheduleJobResponse;
import org.recap.quartz.SchedulerService;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class ScheduleJobsControllerUT extends BaseControllerUT {

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
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        ScheduleJobRequest scheduleJobRequest = new ScheduleJobRequest();
        scheduleJobRequest.setJobName(jobName);
        scheduleJobRequest.setScheduleType(ScsbConstants.SCHEDULE);
        scheduleJobRequest.setCronExpression(cronExpression);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = this.mockMvc.perform(post("/scheduleService/scheduleJob")
                .headers(getHttpHeaders())
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(scheduleJobRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertNotNull(result);
    }

    @Test
    public void testScheduleJob_reschedule() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        ScheduleJobRequest scheduleJobRequest = new ScheduleJobRequest();
        scheduleJobRequest.setJobName(jobName);
        scheduleJobRequest.setScheduleType(ScsbConstants.RESCHEDULE);
        scheduleJobRequest.setCronExpression(cronExpression);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = this.mockMvc.perform(post("/scheduleService/scheduleJob")
                .headers(getHttpHeaders())
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(scheduleJobRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertNotNull(result);
    }
    @Test
    public void testScheduleJob_unschedule() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        ScheduleJobRequest scheduleJobRequest = new ScheduleJobRequest();
        scheduleJobRequest.setJobName(jobName);
        scheduleJobRequest.setScheduleType(ScsbConstants.UNSCHEDULE);
        scheduleJobRequest.setCronExpression(cronExpression);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = this.mockMvc.perform(post("/scheduleService/scheduleJob")
                .headers(getHttpHeaders())
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(scheduleJobRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertNotNull(result);
    }

    @Test
    public void testScheduleJob_Exception() {
        Mockito.when(scheduleJobRequest.getScheduleType()).thenReturn(ScsbConstants.SCHEDULE);
        Mockito.when(schedulerService.scheduleJob(null,null)).thenThrow(NullPointerException.class);
        ScheduleJobResponse scheduleJobResponse=mockscheduleJobsController.scheduleJob(scheduleJobRequest);
        assertNull(scheduleJobResponse.getMessage());
    }
}
