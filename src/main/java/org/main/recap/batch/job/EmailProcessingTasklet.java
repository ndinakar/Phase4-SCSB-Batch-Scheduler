package org.main.recap.batch.job;

import org.main.recap.batch.service.EmailService;
import org.main.recap.jpa.JobDetailsRepository;
import org.main.recap.model.EmailPayLoad;
import org.main.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
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

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Sending Email");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();

        JobEntity jobEntity = jobDetailsRepository.findByJobName(jobName);

        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobName);
        emailPayLoad.setJobDescription(jobEntity.getJobDescription());
        emailPayLoad.setStartDate(createdDate);
        emailPayLoad.setStatus("Successfully");
        String result = emailService.sendEmail(serverProtocol, solrClientUrl, emailPayLoad);
        logger.info("Email sending - {}", result);
        return RepeatStatus.FINISHED;
    }
}
