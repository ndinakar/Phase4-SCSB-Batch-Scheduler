package org.recap.quartz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.job.JobCommonTasklet;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.job.JobDto;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.quartz.CronExpression.isValidExpression;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Slf4j
@Component
public class QuartzJobsInitializer {

    private JobLauncher jobLauncher;
    private JobLocator jobLocator;
    private ScsbJobService scsbJobService;
    private Scheduler scheduler;

    /**
     * Instantiates a new Quartz jobs initializer.
     *
     * @param jobLauncher          the job launcher
     * @param jobLocator           the job locator
     * @param scsbJobService       the scsb job service
     * @param scheduler            the scheduler
     */
    @Autowired
    public QuartzJobsInitializer(JobLauncher jobLauncher, JobLocator jobLocator, ScsbJobService scsbJobService, Scheduler scheduler) {
        this.jobLauncher = jobLauncher;
        this.jobLocator = jobLocator;
        this.scsbJobService = scsbJobService;
        this.scheduler = scheduler;
        initializeJobs();
    }

    /**
     * This method reads the jobs from database and initializes them with the quartz scheduler.
     * The jobs without cron expression or unscheduled state are added to scheduler without any trigger.
     * The jobs with cron expression are added to scheduler with a trigger.
     */
    public void initializeJobs() {
        log.info("Initializing jobs");
        try {
            List<JobDto> jobDtoList = scsbJobService.getAllJobs();
            if (CollectionUtils.isNotEmpty(jobDtoList)) {
                for (JobDto jobDto : jobDtoList) {
                    String jobName = jobDto.getJobName();
                    String jobStatus = jobDto.getStatus();
                    String cronExpression = jobDto.getCronExpression();
                    try {
                        JobDetailImpl jobDetailImpl = new JobDetailImpl();
                        JobCommonTasklet jobCommonTasklet = new JobCommonTasklet();
                        jobCommonTasklet.setJobDetailImpl(jobDetailImpl, jobName, jobLauncher, jobLocator);
                        if (StringUtils.isNotBlank(cronExpression) && isValidExpression(cronExpression) && !ScsbConstants.UNSCHEDULED.equalsIgnoreCase(jobStatus)) {
                            JobKey jobKey = new JobKey(jobName);
                            jobDetailImpl.setKey(jobKey);
                            CronTriggerImpl trigger = new CronTriggerImpl();
                            trigger.setName(jobName + ScsbConstants.TRIGGER_SUFFIX);
                            trigger.setJobKey(jobKey);
                            trigger.setCronExpression(cronExpression);
                            scheduler.scheduleJob(jobDetailImpl, trigger);
                            log.info("Job {} is initialized.", jobName);
                        } else {
                            log.info("Job {} has invalid cron expression and unscheduled state.", jobName);
                            JobKey jobKey = new JobKey(jobName);
                            jobDetailImpl.setKey(jobKey);
                            jobDetailImpl.setDurability(true);
                            scheduler.addJob(jobDetailImpl, true);
                        }
                    } catch (Exception ex) {
                        log.error("Initializing job {} Failed.", jobName);
                        log.error(ScsbCommonConstants.LOG_ERROR, ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Failed to initialize jobs to Quartz Scheduler. Could not fetch jobs from server.");
            log.error(ScsbCommonConstants.LOG_ERROR, ex);
        }
    }
}
