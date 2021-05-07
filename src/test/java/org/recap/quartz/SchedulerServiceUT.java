package org.recap.quartz;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class SchedulerServiceUT extends BaseTestCaseUT {

    @InjectMocks
    SchedulerService schedulerService;

    @Mock
    Scheduler scheduler;

    @Mock
    JobDetail jobDetail;

    @Mock
    CronTriggerImpl trigger;

    @Test
    public void testScheduleJobInvalidCron() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "1 2 3 4 5";
        String message = schedulerService.scheduleJob(jobName, cronExpression);
        assertNotNull(message);
        assertEquals(ScsbConstants.ERROR_INVALID_CRON_EXPRESSION, message);
    }

    @Test
    public void testScheduleJobScheduled() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        Mockito.when(scheduler.getJobDetail(Mockito.any())).thenReturn(jobDetail);
        String message = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertEquals(ScsbConstants.JOB_SUCCESS_SCHEDULING, message);
    }

    @Test
    public void testScheduleJobScheduledException() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        Mockito.when(scheduler.getJobDetail(Mockito.any())).thenReturn(jobDetail);
        Mockito.when(scheduler.scheduleJob(Mockito.any())).thenThrow(NullPointerException.class);
        String message = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertEquals(ScsbConstants.ERROR_JOB_FAILED_SCHEDULING, message);
    }

    @Test
    public void testScheduleJobRunEveryFiveSeconds() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String message = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertEquals(ScsbConstants.JOB_SUCCESS_SCHEDULING, message);
    }

    @Test
    public void testRescheduleJobForUnscheduledJob() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        String message = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(message);
        assertEquals(ScsbConstants.ERROR_JOB_FAILED_RESCHEDULING, message);
    }

    @Test
    public void testRescheduleJobRunEveryFiveSecondsForScheduledJob() throws Exception {
        Mockito.when(scheduler.getTrigger(Mockito.any())).thenReturn(trigger);
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String scheduleMessage = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(scheduleMessage);
        String cronExpression = "0/10 * * * * ? *";
        String rescheduleMessage = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(rescheduleMessage);
        assertEquals(ScsbConstants.JOB_SUCCESS_RESCHEDULING, rescheduleMessage);
    }

    @Test
    public void testRescheduleJobRunEveryFiveSecondsInvalid() throws Exception {
        Mockito.when(scheduler.getTrigger(Mockito.any())).thenReturn(trigger);
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String scheduleMessage = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(scheduleMessage);
        String cronExpression = "1 2 3 4 5";
        String rescheduleMessage = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(rescheduleMessage);
        assertEquals(ScsbConstants.ERROR_INVALID_CRON_EXPRESSION, rescheduleMessage);
    }

    @Test
    public void testUnscheduleJob() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String scheduleMessage = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(scheduleMessage);
        String message = schedulerService.unscheduleJob(jobName);
        assertNotNull(message);
        assertEquals(ScsbConstants.JOB_SUCCESS_UNSCHEDULING, message);
    }

    @Test
    public void testUnscheduleJobException() throws Exception {
        String jobName = ScsbCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String scheduleMessage = schedulerService.scheduleJob(jobName, "1 2 3 4 5");
        assertNotNull(scheduleMessage);
        Mockito.when(scheduler.unscheduleJob(Mockito.any())).thenThrow(NullPointerException.class);
        String message = schedulerService.unscheduleJob(jobName);
        assertNotNull(message);
        assertEquals(ScsbConstants.ERROR_JOB_FAILED_UNSCHEDULING, message);
    }
}
