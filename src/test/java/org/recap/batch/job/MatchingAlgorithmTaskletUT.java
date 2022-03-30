package org.recap.batch.job;

import org.apache.camel.*;
import org.apache.camel.spi.RouteController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.ScsbCommonConstants;
import org.recap.batch.service.UpdateJobDetailsService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest
public class MatchingAlgorithmTaskletUT  {

    @InjectMocks
    MatchingAlgorithmTasklet matchingAlgorithmTasklet;

    @Mock
    MatchingAlgorithmTasklet mockmatchingAlgorithmTasklet;

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

    @Before
    public  void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testexecute_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        message.setMessageId("1");
        message.setBody(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId() ;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(ScsbCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
        requestMap.put(ScsbCommonConstants.PROCESS_TYPE, ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        requestMap.put(ScsbCommonConstants.CREATED_DATE, createdDate.toString());
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"camelContext",camelContext);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"updateJobDetailsService",updateJobDetailsService);
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        Mockito.doNothing().when(mockmatchingAlgorithmTasklet).updateJob(jobExecution, "Matching Algorithm Tasklet", Boolean.TRUE);
        RepeatStatus status = matchingAlgorithmTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        message.setMessageId("1");
        message.setBody(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId() +"|" + RepeatStatus.FINISHED;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(ScsbCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
        requestMap.put(ScsbCommonConstants.PROCESS_TYPE, ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        requestMap.put(ScsbCommonConstants.CREATED_DATE, createdDate.toString());
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"camelContext",camelContext);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"updateJobDetailsService",updateJobDetailsService);
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        Mockito.doNothing().when(mockmatchingAlgorithmTasklet).updateJob(jobExecution, "Matching Algorithm Tasklet", Boolean.TRUE);
        RepeatStatus status = matchingAlgorithmTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute_fail() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        message.setMessageId("1");
        message.setBody(ScsbCommonConstants.JOB_ID + ":" + jobExecution.getId());
        exchange.setIn(message);
        String resultStatus = "" + RepeatStatus.FINISHED;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(ScsbCommonConstants.JOB_ID, String.valueOf(jobExecution.getId()));
        requestMap.put(ScsbCommonConstants.PROCESS_TYPE, ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        requestMap.put(ScsbCommonConstants.CREATED_DATE, createdDate.toString());
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"producerTemplate",producerTemplate);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"camelContext",camelContext);
        ReflectionTestUtils.setField(matchingAlgorithmTasklet,"updateJobDetailsService",updateJobDetailsService);
        Mockito.when(camelContext.getRouteController()).thenReturn(routeController);
        Mockito.doNothing().when(routeController).startRoute(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE);
        Mockito.when(camelContext.getEndpoint(ScsbCommonConstants.MATCHING_ALGORITHM_JOB_COMPLETION_OUTGOING_QUEUE)).thenReturn(endpoint);
        Mockito.when(endpoint.createPollingConsumer()).thenReturn(consumer);
        Mockito.when(consumer.receive()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getBody()).thenReturn(resultStatus);
        Mockito.doNothing().when(mockmatchingAlgorithmTasklet).updateJob(jobExecution, "Matching Algorithm Tasklet", Boolean.TRUE);
        RepeatStatus status = matchingAlgorithmTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
