package org.recap.quartz;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.recap.ScsbCommonConstants;
import org.recap.util.CloudDataFlowTriggerUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class QuartzJobLauncher extends QuartzJobBean {

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;



    /**
     * Quartz scheduler calls this method on each scheduled run of a job.
     * It identifies the spring batch configured job using the job name and holds the job execution details.
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        CloudDataFlowTriggerUtil cloudDataFlowTriggerUtil = new CloudDataFlowTriggerUtil();
        try {
            Job job = jobLocator.getJob(jobName);
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addLong("time", System.currentTimeMillis());
            cloudDataFlowTriggerUtil.launchTask(jobName);
            JobExecution jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
            //log.info("{}_{} was completed successfully. Status : {}", job.getName(), jobExecution.getId(), jobExecution.getStatus());
        } catch (Exception exception) {
            log.error(ScsbCommonConstants.LOG_ERROR, exception);
        }
    }
}
