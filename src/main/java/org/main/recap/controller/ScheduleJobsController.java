package org.main.recap.controller;

import org.main.recap.RecapConstants;
import org.main.recap.model.ScheduleJobRequest;
import org.main.recap.model.ScheduleJobResponse;
import org.main.recap.quartz.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rajeshbabuk on 5/4/17.
 */

@RestController
@RequestMapping("/scheduleService")
public class ScheduleJobsController {

    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping(value="/scheduleJob", method = RequestMethod.POST)
    public ScheduleJobResponse scheduleJob(@RequestBody ScheduleJobRequest scheduleJobRequest) {
        ScheduleJobResponse scheduleJobResponse = new ScheduleJobResponse();
        if (RecapConstants.SCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
            String message = schedulerService.scheduleJob(scheduleJobRequest.getJobName(), scheduleJobRequest.getCronExpression());
            scheduleJobResponse.setMessage(message);
        } else if (RecapConstants.RESCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
            String message = schedulerService.rescheduleJob(scheduleJobRequest.getJobName(), scheduleJobRequest.getCronExpression());
            scheduleJobResponse.setMessage(message);
        } else if (RecapConstants.UNSCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
            String message = schedulerService.unscheduleJob(scheduleJobRequest.getJobName());
            scheduleJobResponse.setMessage(message);
        }
        return scheduleJobResponse;
    }
}
