package org.recap.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.ScheduleJobRequest;
import org.recap.model.ScheduleJobResponse;
import org.recap.quartz.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by rajeshbabuk on 5/4/17.
 */
@Slf4j
@RestController
@RequestMapping("/scheduleService")
public class ScheduleJobsController {

    @Autowired
    private SchedulerService schedulerService;

    /**
     * This method is exposed as scheduler service for other microservices to schedule or reschedule or unschedule a job,
     *
     * @param scheduleJobRequest the schedule job request
     * @return the schedule job response with the status message and next execution time.
     */
    @PostMapping(value = "/scheduleJob")
    public ScheduleJobResponse scheduleJob(@RequestBody ScheduleJobRequest scheduleJobRequest) {
        ScheduleJobResponse scheduleJobResponse = new ScheduleJobResponse();
        String message = null;
        try {
            if (ScsbConstants.SCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
                message = schedulerService.scheduleJob(scheduleJobRequest.getJobName(), scheduleJobRequest.getCronExpression());
            } else if (ScsbConstants.RESCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
                message = schedulerService.rescheduleJob(scheduleJobRequest.getJobName(), scheduleJobRequest.getCronExpression());
            } else if (ScsbConstants.UNSCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
                message = schedulerService.unscheduleJob(scheduleJobRequest.getJobName());
            }
            if (StringUtils.containsIgnoreCase(message, ScsbConstants.SUCCESS) && !ScsbConstants.UNSCHEDULE.equals(scheduleJobRequest.getScheduleType())) {
                CronExpression cronExpression = new CronExpression(scheduleJobRequest.getCronExpression());
                scheduleJobResponse.setNextRunTime(cronExpression.getNextValidTimeAfter(new Date()));
            }
        } catch (Exception exception) {
            log.error(ScsbCommonConstants.LOG_ERROR, exception);
            message = exception.getMessage();
        }
        scheduleJobResponse.setMessage(message);
        return scheduleJobResponse;
    }
}
