package org.main.recap.batch.job;

import org.main.recap.batch.service.DailyReconcilationService;
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
 * Created by akulak on 10/5/17.
 */
public class DailyReconcilationTasklet implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(EmailProcessingTasklet.class);

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.circ.url}")
    String solrCircUrl;

    @Autowired
    private DailyReconcilationService dailyReconcilationService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DailyReconcilation");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(serverProtocol, solrCircUrl, jobName, createdDate);
        dailyReconcilationService.dailyReconcilation(serverProtocol, solrCircUrl,jobName,createdDate);
        return RepeatStatus.FINISHED;
    }
}
