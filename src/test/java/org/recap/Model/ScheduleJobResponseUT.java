package org.recap.Model;

import lombok.Data;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.recap.BaseTestCase;
import org.recap.model.ScheduleJobResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

@Data
public class ScheduleJobResponseUT extends BaseTestCase
{
    @Test
    public void ScheduleJobResponse() throws  Exception
    {
        String message = "test";
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        ScheduleJobResponse scheduleJobResponse = new ScheduleJobResponse();
        scheduleJobResponse.setMessage(message);
        scheduleJobResponse.setNextRunTime(dNow);
        assertNotNull(scheduleJobResponse.getMessage());
        assertNotNull(scheduleJobResponse.getNextRunTime());

    }
}
