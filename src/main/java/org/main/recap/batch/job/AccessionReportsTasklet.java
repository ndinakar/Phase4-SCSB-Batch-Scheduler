package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.GenerateReportsService;
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

import java.util.Calendar;
import java.util.Date;

/**
 * Created by angelind on 2/5/17.
 */
public class AccessionReportsTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(AccessionReportsTasklet.class);

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Autowired
    private GenerateReportsService generateReportsService;

    /**
     * This method starts the execution of the accession reports job.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing AccessionReportsTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Accession Reports Tasklet : {}", jobNameParam);
        if(!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
        }
        String status = generateReportsService.generateReport(solrClientUrl, getFromDate(createdDate), RecapConstants.GENERATE_ACCESSION_REPORT_JOB);
        logger.info("Accession Report status : {}", status);
        return RepeatStatus.FINISHED;
    }

    /**
     * This method builds the from date using the current date whose time would be 00:00:00
     *
     * @param createdDate the created date
     * @return the from date
     */
    public Date getFromDate(Date createdDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
}
