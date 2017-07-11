package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.EmailService;
import org.main.recap.jpa.JobDetailsRepository;
import org.main.recap.model.EmailPayLoad;
import org.main.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
public class EmailProcessingTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(EmailProcessingTasklet.class);

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    /**
     * This method starts the execution of the email processing job.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Sending Email");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobStatus = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_STATUS);
        String jobStatusMessage = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_STATUS_MESSAGE);

        JobEntity jobEntity = jobDetailsRepository.findByJobName(jobName);

        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobName);
        emailPayLoad.setJobDescription(jobEntity.getJobDescription());
        emailPayLoad.setStartDate(createdDate);
        emailPayLoad.setStatus(jobStatus);
        emailPayLoad.setMessage(jobStatusMessage);

        String result = emailService.sendEmail(solrClientUrl, emailPayLoad);
        logger.info("Email sending - {}", result);
        return RepeatStatus.FINISHED;
    }
}
