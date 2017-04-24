package org.main.recap.quartz;

import org.junit.Test;
import org.main.recap.BaseTestCase;
import org.main.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 19/4/17.
 */
public class SchedulerServiceUT extends BaseTestCase {

    @Autowired
    SchedulerService schedulerService;

    @Test
    public void testScheduleJob() throws Exception {
        String jobName = RecapConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        String message = schedulerService.scheduleJob(jobName, cronExpression);
        assertNotNull(message);
    }

    @Test
    public void testRescheduleJob() throws Exception {
        String jobName = RecapConstants.PURGE_EXCEPTION_REQUESTS;
        String cronExpression = "0/10 * * * * ? *";
        String message = schedulerService.rescheduleJob(jobName, cronExpression);
        assertNotNull(message);
    }

    @Test
    public void testUnscheduleJob() throws Exception {
        String jobName = RecapConstants.PURGE_EXCEPTION_REQUESTS;
        String message = schedulerService.unscheduleJob(jobName);
        assertNotNull(message);
    }
}
