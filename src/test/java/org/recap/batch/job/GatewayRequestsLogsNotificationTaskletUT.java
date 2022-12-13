package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.CommonService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GatewayRequestsLogsNotificationTaskletUT extends BaseTestCase {

    @Mock
    GatewayRequestsLogsNotificationTasklet logsNotificationTasklet;
    @Mock
    CommonService commonService ;

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    String scsbEtlUrl;

    @Test
    public void executeTest() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("GatewayRequestsLogsNotificationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(commonService.requestLog(scsbEtlUrl, ScsbConstants.GATEWAY_REQUEST_LOG_FOR_EMAIL_NOTIFICATION)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(logsNotificationTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = logsNotificationTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void executeFailedTest() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("GatewayRequestsLogsNotificationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(commonService.requestLog(scsbEtlUrl, ScsbConstants.GATEWAY_REQUEST_LOG_FOR_EMAIL_NOTIFICATION)).thenReturn(ScsbConstants.FAILURE);
        Mockito.when(logsNotificationTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = logsNotificationTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }



}
