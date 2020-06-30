package org.recap.quartz;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class SchedulerServiceUT extends BaseTestCase {

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    Scheduler scheduler;

    @Test
    public void testScheduleJobInvalidCron() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "1 2 3 4 5";
        String message = schedulerService.scheduleJob(jobName, cronExpression);
        assertNotNull(message);
        assertEquals(RecapConstants.ERROR_INVALID_CRON_EXPRESSION, message);
    }

    @Test
    public void testScheduleJobRunEveryFiveSeconds() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        TriggerKey triggerKey = new TriggerKey(jobName + RecapConstants.TRIGGER_SUFFIX);
        String message = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(message);
        boolean isTriggerExistsAfterSchedule = scheduler.checkExists(triggerKey);
        assertTrue(isTriggerExistsAfterSchedule);
        Thread.sleep(10000);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        boolean mayFireAgain = trigger.mayFireAgain();
        Date previousFireTime = trigger.getPreviousFireTime();
        Date nextFireTime = trigger.getNextFireTime();
        assertEquals(trigger.getJobKey().getName(), jobName);
        assertTrue(mayFireAgain);
        assertNotNull(previousFireTime);
        assertNotNull(nextFireTime);
    }

    @Test
    public void testRescheduleJobInvalidCron() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "1 2 3 4 5";
        String message = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(message);
        assertEquals(RecapConstants.ERROR_INVALID_CRON_EXPRESSION, message);
    }

    @Test
    public void testRescheduleJobForUnscheduledJob() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        String message = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(message);
        assertEquals(RecapConstants.ERROR_JOB_FAILED_RESCHEDULING, message);
    }

    @Test
    public void testRescheduleJobRunEveryFiveSecondsForScheduledJob() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        TriggerKey triggerKey = new TriggerKey(jobName + RecapConstants.TRIGGER_SUFFIX);
        String scheduleMessage = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(scheduleMessage);
        boolean isTriggerExistsAfterSchedule = scheduler.checkExists(triggerKey);
        assertTrue(isTriggerExistsAfterSchedule);
        String cronExpression = "0/10 * * * * ? *";
        String rescheduleMessage = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(rescheduleMessage);
        assertEquals(RecapConstants.JOB_SUCCESS_RESCHEDULING, rescheduleMessage);
        boolean isTriggerExistsAfterReschedule = scheduler.checkExists(triggerKey);
        assertTrue(isTriggerExistsAfterReschedule);
        Thread.sleep(10000);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        boolean mayFireAgain = trigger.mayFireAgain();
        Date previousFireTime = trigger.getPreviousFireTime();
        Date nextFireTime = trigger.getNextFireTime();
        assertEquals(trigger.getJobKey().getName(), jobName);
        assertTrue(mayFireAgain);
        assertNotNull(previousFireTime);
        assertNotNull(nextFireTime);
    }

    @Test
    public void testUnscheduleJob() throws Exception {
        String jobName = RecapCommonConstants.PURGE_EXCEPTION_REQUESTS;
        TriggerKey triggerKey = new TriggerKey(jobName + RecapConstants.TRIGGER_SUFFIX);
        String scheduleMessage = schedulerService.scheduleJob(jobName, "0/5 * * * * ? *");
        assertNotNull(scheduleMessage);
        boolean isTriggerExistsAfterSchedule = scheduler.checkExists(triggerKey);
        assertTrue(isTriggerExistsAfterSchedule);
        String message = schedulerService.unscheduleJob(jobName);
        assertNotNull(message);
        assertEquals(RecapConstants.JOB_SUCCESS_UNSCHEDULING, message);
        boolean isTriggerExistsAfterUnschedule = scheduler.checkExists(triggerKey);
        assertFalse(isTriggerExistsAfterUnschedule);
    }
}
