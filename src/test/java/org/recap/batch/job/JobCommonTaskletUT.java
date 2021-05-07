package org.recap.batch.job;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.RouteController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.UpdateJobDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class JobCommonTaskletUT extends BaseTestCaseUT {

    private static final Logger logger = LoggerFactory.getLogger(JobCommonTaskletUT.class);

    @InjectMocks
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

    @Mock
    UpdateJobDetailsService updateJobDetailsService;

    @Mock
    JobExecution jobExecution;

    @Mock
    JobParameters jobParameters;

    @Test
    public void getResultStatus() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        message.setMessageId("1");
        message.setBody(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId() +"|" + RepeatStatus.FINISHED;
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, ScsbCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, ScsbConstants.ACCESSION_STATUS_NAME);
        assertEquals("FINISHED",status);
    }

    @Test
    public void getResultStatus_fail() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        message.setMessageId("1");
        message.setBody(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = "test" + RepeatStatus.FINISHED;
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, ScsbCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, ScsbConstants.ACCESSION_STATUS_NAME);
        assertEquals(ScsbConstants.FAILURE + " - " + ScsbConstants.FAILURE_QUEUE_MESSAGE,status);
    }

    @Test
    public void getResultStatus_Exception() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Date createdDate = jobExecution.getCreateTime();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(ScsbCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
        requestMap.put(ScsbCommonConstants.PROCESS_TYPE, ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        requestMap.put(ScsbCommonConstants.CREATED_DATE, createdDate.toString());
        String status = jobCommonTasklet.getResultStatus(jobExecution, execution, logger, executionContext, ScsbCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, ScsbConstants.ACCESSION_STATUS_NAME);
        assertNull(status);
    }

    @Test
    public void setExecutionContext() {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        ExecutionContext status = jobCommonTasklet.setExecutionContext( executionContext,execution, ScsbConstants.SUCCESS);
        assertNotNull(status);
    }
    @Test
    public void setExecutionContext_fail() {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        ExecutionContext status = jobCommonTasklet.setExecutionContext( executionContext,execution,ScsbCommonConstants.FAIL);
        assertNotNull(status);
    }

    @Test
    public void updateJob() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        JobExecution jobExecution = execution.getJobExecution();
        jobCommonTasklet.updateJob(jobExecution,"taskletName",false);
        assertTrue(true);
    }

    @Test
    public void getCreatedDate() throws Exception {
        Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(ScsbConstants.FROM_DATE)).thenReturn("2021-01-01");
        Date createdDate = jobCommonTasklet.getCreatedDate(jobExecution);
        assertNotNull(createdDate);
    }

    @Test
    public void getCreatedDateParseException() throws Exception {
        Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(ScsbConstants.FROM_DATE)).thenReturn(new Date().toString());
        Date createdDate = jobCommonTasklet.getCreatedDate(jobExecution);
        assertNull(createdDate);
    }

}
