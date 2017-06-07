package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.AccessionService;
import org.main.recap.batch.service.UpdateJobDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by angelind on 9/5/17.
 */
public class AccessionTasklet implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(AccessionTasklet.class);

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Autowired
    private AccessionService accessionService;

    /**
     * This method starts the execution of the accession job.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing AccessionReportsTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Accession Tasklet : {}", jobNameParam);
        if(!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate);
        }

        String status = accessionService.processAccession(solrClientUrl, createdDate);
        logger.info("Accession status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
