package org.main.recap.batch.job;

import org.main.recap.batch.service.AccessionReconcilationService;
import org.main.recap.batch.service.UpdateJobDetailsService;
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
 * Created by akulak on 19/5/17.
 */
public class AccessionReconcilationTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(AccessionReconcilationTasklet.class);

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Autowired
    private AccessionReconcilationService accessionReconcilationService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing Accession Reconcilation");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(serverProtocol, solrClientUrl, jobName, createdDate);
        accessionReconcilationService.accessionReconcilation(serverProtocol, scsbCircUrl);
        return RepeatStatus.FINISHED;
    }
}
