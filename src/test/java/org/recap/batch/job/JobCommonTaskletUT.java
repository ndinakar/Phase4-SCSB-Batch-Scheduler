package org.recap.batch.job;

import org.apache.camel.*;
import org.apache.camel.spi.RouteController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(PowerMockRunner.class)
@PrepareForTest
public class JobCommonTaskletUT {

    private static final Logger logger = LoggerFactory.getLogger(JobCommonTaskletUT.class);

    @Mock
    JobCommonTasklet jobCommonTasklet;

    @Mock
    CamelContext camelContext;

    @Mock
    ProducerTemplate producerTemplate;

    @Mock
    Endpoint endpoint;

    @Mock
    PollingConsumer consumer;

    @Mock
    Exchange exchange;

    @Mock
    RouteController routeController;

    @Mock
    Message message;


    @Test
    public void getResultStatus() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Date createdDate = jobExecution.getCreateTime();
        message.setMessageId("1");
        message.setBody(RecapCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = RecapCommonConstants.JOB_ID + ":" + jobExecution.getId() +"|" + RepeatStatus.FINISHED;
        ReflectionTestUtils.setField(jobCommonTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(jobCommonTasklet,"camelContext",camelContext);
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        Mockito.when(jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME)).thenCallRealMethod();
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME);

    }

    @Test
    public void getResultStatus_fail() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        message.setMessageId("1");
        message.setBody(RecapCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = "test" + RepeatStatus.FINISHED;
        ReflectionTestUtils.setField(jobCommonTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(jobCommonTasklet,"camelContext",camelContext);
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        Mockito.when(jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME)).thenCallRealMethod();
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME);

    }

    @Test
    public void getResultStatus_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Date createdDate = jobExecution.getCreateTime();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(RecapCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
        requestMap.put(RecapCommonConstants.PROCESS_TYPE, RecapCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        requestMap.put(RecapCommonConstants.CREATED_DATE, createdDate.toString());
        ReflectionTestUtils.setField(jobCommonTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(jobCommonTasklet,"camelContext",camelContext);
        Mockito.when(jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME)).thenCallRealMethod();
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, RecapCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, RecapCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, RecapConstants.ACCESSION_STATUS_NAME);

    }

    @Test
    public void setExecutionContext() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(jobCommonTasklet.setExecutionContext(  executionContext,execution,RecapConstants.SUCCESS)).thenCallRealMethod();
        ExecutionContext status = jobCommonTasklet.setExecutionContext( executionContext,execution,RecapConstants.SUCCESS);
        assertNotNull(status);
    }
    @Test
    public void setExecutionContext_fail() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(jobCommonTasklet.setExecutionContext(  executionContext,execution,RecapCommonConstants.FAIL)).thenCallRealMethod();
        ExecutionContext status = jobCommonTasklet.setExecutionContext( executionContext,execution,RecapCommonConstants.FAIL);
        assertNotNull(status);
    }

}
