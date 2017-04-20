package org.main.recap.quartz;

import org.junit.Test;
import org.main.recap.BaseTestCase;
import org.main.recap.RecapConstants;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * Created by rajeshbabuk on 20/4/17.
 */
public class QuartzJobsInitializerUT extends BaseTestCase {

    @Autowired
    QuartzJobsInitializer quartzJobsInitializer;

    @Autowired
    Scheduler scheduler;

    @Test
    public void testInitializeJobs() throws Exception {
        quartzJobsInitializer.initializeJobs();
        JobKey jobKey = new JobKey(RecapConstants.PURGE_EXCEPTION_REQUESTS);
        boolean exists = scheduler.checkExists(jobKey);
        assertTrue(exists);

    }
}
