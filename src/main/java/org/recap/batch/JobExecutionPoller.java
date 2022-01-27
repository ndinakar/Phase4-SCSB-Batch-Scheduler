package org.recap.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.EmailService;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.EmailPayLoad;
import org.recap.model.job.JobDto;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by rajeshbabuk on 28/7/17.
 */
@Slf4j
@Component
public class JobExecutionPoller {

    private Long jobExecutionPollerTime;
    private JobExplorer jobExplorer;
    private String solrClientUrl;
    private EmailService emailService;
    private ScsbJobService scsbJobService;

    /**
     * Instantiates a new Job execution poller.
     *
     * @param jobExecutionPollerTime the job execution poller time
     * @param jobExplorer            the job explorer
     * @param solrClientUrl          the solr client url
     * @param emailService           the email service
     * @param scsbJobService         the scsb job service
     */
    @Autowired
    public JobExecutionPoller(@Value("${" + PropertyKeyConstants.LONG_RUNNING_JOBS_POLLER_TIME_IN_MINUTES + "}") Long jobExecutionPollerTime, JobExplorer jobExplorer, @Value("${" + PropertyKeyConstants.SCSB_SOLR_DOC_URL + "}") String solrClientUrl, EmailService emailService, ScsbJobService scsbJobService) {
        this.jobExecutionPollerTime = jobExecutionPollerTime;
        this.jobExplorer = jobExplorer;
        this.solrClientUrl = solrClientUrl;
        this.emailService = emailService;
        this.scsbJobService = scsbJobService;
        new JobExecutionThread().start();
    }

    private class JobExecutionThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * jobExecutionPollerTime * 60);
                    if (ScsbConstants.POLL_LONG_RUNNING_JOBS) {
                        sendNotificationMailForLongRunningJobs();
                    }
                } catch (Exception ex) {
                    log.error(ScsbCommonConstants.LOG_ERROR, ex);
                }
            }
        }

        /**
         * Sends an email notification if the jobs are running more than the configured time.
         * @throws NoSuchJobException
         */
        private void sendNotificationMailForLongRunningJobs() {
            List<String> jobNames = jobExplorer.getJobNames();
            if (CollectionUtils.isNotEmpty(jobNames)) {
                for (String jobName : jobNames) {
                    Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(jobName);
                    if (CollectionUtils.isNotEmpty(runningJobExecutions)) {
                        JobDto jobDto = scsbJobService.getJobByName(jobName);
                        for (JobExecution jobExecution : runningJobExecutions) {
                            long diffTime = new Date().getTime() - jobExecution.getStartTime().getTime();
                            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffTime);
                            if (diffMinutes >= jobExecutionPollerTime) {
                                EmailPayLoad emailPayLoad = new EmailPayLoad();
                                emailPayLoad.setJobName(jobName);
                                emailPayLoad.setJobDescription(null != jobDto ? jobDto.getJobDescription() : null);
                                emailPayLoad.setJobAction(ScsbConstants.STARTED);
                                emailPayLoad.setStartDate(jobExecution.getStartTime());
                                emailPayLoad.setStatus(getRunningStatus(diffTime));
                                emailPayLoad.setMessage("Job with execution id \'" + jobExecution.getId() + "\' is running longer than the anticipated time.");
                                String result = emailService.sendEmail(solrClientUrl, emailPayLoad);
                                log.info("Email sending - {}", result);
                            }
                        }
                    }
                }
            }
        }

        /**
         * Returns job status with difference in time.
         * @param diffTime
         * @return
         */
        private String getRunningStatus(long diffTime) {
            String jobStatus = "Running since ";
            long diffHours = TimeUnit.MILLISECONDS.toHours(diffTime);
            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffTime);
            long diffDays = TimeUnit.MILLISECONDS.toDays(diffTime);
            if (diffDays > 0) {
                jobStatus = jobStatus + diffDays + " day(s).";
            } else if (diffHours > 0) {
                jobStatus = jobStatus + diffHours + " hour(s).";
            } else if (diffMinutes > 0) {
                jobStatus = jobStatus + diffMinutes + " minute(s).";
            } else {
                jobStatus = ScsbConstants.RUNNING;
            }
            return jobStatus;
        }
    }
}
