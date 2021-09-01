package org.recap.batch.job;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.commons.lang.StringUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
public class MatchingAlgorithmTasklet extends  JobCommonTasklet implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmTasklet.class);

    /**
     * This method starts the execution of the matching algorithm job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing MatchingAlgorithmTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        PollingConsumer consumer = null;
        try {
            Date createdDate = getCreatedDate(jobExecution);
            updateJob(jobExecution, "Matching Algorithm Tasklet", Boolean.TRUE);
            
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put(ScsbCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
            requestMap.put(ScsbCommonConstants.PROCESS_TYPE, ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
            requestMap.put(ScsbCommonConstants.CREATED_DATE, createdDate.toString());
            producerTemplate.sendBody(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_INITIATE_QUEUE, requestMap);
            Endpoint endpoint = camelContext.getEndpoint(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE);
            consumer = endpoint.createPollingConsumer();
            Exchange exchange = consumer.receive();
            String resultStatus = (String) exchange.getIn().getBody();
            if (StringUtils.isNotBlank(resultStatus)) {
                String[] resultSplitMessage = resultStatus.split("\\|");
                if (!resultSplitMessage[0].equalsIgnoreCase(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId())) {
                    producerTemplate.sendBody(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE, resultStatus);
                    resultStatus = ScsbConstants.FAILURE + " - " + ScsbConstants.FAILURE_QUEUE_MESSAGE;
                } else {
                    resultStatus = resultSplitMessage[1];
                }
            }
            logger.info("Job Id : {} Matching Algorithm Job Result Status : {}", jobExecution.getId(), resultStatus);
            setExecutionContext(executionContext, stepExecution, ScsbConstants.MATCHING_ALGORITHM_STATUS_NAME + " " + resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.MATCHING_ALGORITHM_STATUS_NAME);
        }
        finally {
            if(consumer != null) {
                consumer.close();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
