package org.recap.Model;

import lombok.Data;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.ScheduleJobRequest;

import static org.junit.Assert.assertNotNull;

@Data
public class ScheduleJobRequestUT extends BaseTestCase
{
    @Test
    public void ScheduleJobRequest()
    {
        Integer jobId = 1;
        String jobName = "test";
        String cronExpression = "test";
        String scheduleType = "pre";

        ScheduleJobRequest scheduleJobRequest = new ScheduleJobRequest();
        scheduleJobRequest.setJobId(jobId);
        scheduleJobRequest.setJobName(jobName);
        scheduleJobRequest.setCronExpression(cronExpression);
        scheduleJobRequest.setScheduleType(scheduleType);

        assertNotNull(scheduleJobRequest.getJobId());
        assertNotNull(scheduleJobRequest.getScheduleType());
        assertNotNull(scheduleJobRequest.getJobName());
        assertNotNull(scheduleJobRequest.getCronExpression());
    }
}
