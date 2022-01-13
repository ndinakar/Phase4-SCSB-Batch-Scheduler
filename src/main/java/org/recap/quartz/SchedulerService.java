package org.recap.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.job.JobCommonTasklet;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.quartz.CronExpression.isValidExpression;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    /**
     * This method is used to schedule a job with the given job name and add a trigger with the given cron expression.
     *
     * @param jobName        the job name
     * @param cronExpression the cron expression
     * @return the string
     */
    public String scheduleJob(String jobName, String cronExpression) {
        try {
            boolean validCronExpression = isValidExpression(cronExpression);
            if (!validCronExpression) {
                return ScsbConstants.ERROR_INVALID_CRON_EXPRESSION;
            } else {
                JobKey jobKey = new JobKey(jobName);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDetailImpl jobDetailImpl = new JobDetailImpl();
                if (null == jobDetail) {
                    JobCommonTasklet jobCommonTasklet = new JobCommonTasklet();
                    jobCommonTasklet.setJobDetailImpl(jobDetailImpl, jobName, jobLauncher, jobLocator);
                   }
                CronTriggerImpl trigger = new CronTriggerImpl();
                trigger.setName(jobName + ScsbConstants.TRIGGER_SUFFIX);
                trigger.setJobKey(jobKey);
                trigger.setCronExpression(cronExpression);
                if (null != jobDetail) {
                    scheduler.scheduleJob(trigger);
                } else {
                    scheduler.scheduleJob(jobDetailImpl, trigger);
                }
            }
        } catch (Exception ex) {
            log.error(ScsbCommonConstants.LOG_ERROR, ex);
            return ScsbConstants.ERROR_JOB_FAILED_SCHEDULING;
        }
        return ScsbConstants.JOB_SUCCESS_SCHEDULING;
    }

    /**
     * This method is used to reschedule a job with the given job name and modify the trigger with the given cron expression.
     *
     * @param jobName        the job name
     * @param cronExpression the cron expression
     * @return the string
     */
    public String rescheduleJob(String jobName, String cronExpression) {
        try {
            boolean validCronExpression = isValidExpression(cronExpression);
            if (!validCronExpression) {
                return ScsbConstants.ERROR_INVALID_CRON_EXPRESSION;
            } else {
                TriggerKey triggerKey = new TriggerKey(jobName + ScsbConstants.TRIGGER_SUFFIX);
                CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
                trigger.setCronExpression(cronExpression);
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception ex) {
            log.error(ScsbCommonConstants.LOG_ERROR, ex);
            return ScsbConstants.ERROR_JOB_FAILED_RESCHEDULING;
        }
        return ScsbConstants.JOB_SUCCESS_RESCHEDULING;
    }

    /**
     * This method is used to unschedule the job with the given job name.
     *
     * @param jobName the job name
     * @return the string
     */
    public String unscheduleJob(String jobName) {
        try {
            TriggerKey triggerKey = new TriggerKey(jobName + ScsbConstants.TRIGGER_SUFFIX);
            scheduler.unscheduleJob(triggerKey);
        } catch (Exception ex) {
            log.error(ScsbCommonConstants.LOG_ERROR, ex);
            return ScsbConstants.ERROR_JOB_FAILED_UNSCHEDULING;
        }
        return ScsbConstants.JOB_SUCCESS_UNSCHEDULING;
    }

}
