package org.recap.batch.job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.EmailService;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.EmailPayLoad;
import org.recap.model.job.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
public class EmailProcessingTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(EmailProcessingTasklet.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private ScsbJobService scsbJobService;

    /**
     * This method starts the execution of the email processing job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Sending Email");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String jobName = jobExecution.getJobInstance().getJobName();
            Date createdDate = jobExecution.getCreateTime();
            String jobStatus = (String) executionContext.get(ScsbConstants.JOB_STATUS);
            String jobStatusMessage = (String) executionContext.get(ScsbConstants.JOB_STATUS_MESSAGE);

            JobDto jobDto = scsbJobService.getJobByName(jobName);

            EmailPayLoad emailPayLoad = new EmailPayLoad();
            emailPayLoad.setJobName(jobName);
            emailPayLoad.setJobDescription(jobDto.getJobDescription());
            emailPayLoad.setJobAction(ScsbConstants.RAN);
            emailPayLoad.setStartDate(createdDate);
            emailPayLoad.setStatus(jobStatus);
            emailPayLoad.setMessage(jobStatusMessage);

            String result = emailService.sendEmail(solrClientUrl, emailPayLoad);
            logger.info("Email sending - {}", result);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS, ScsbConstants.SUCCESS));
        } catch (Exception ex) {
            logger.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
