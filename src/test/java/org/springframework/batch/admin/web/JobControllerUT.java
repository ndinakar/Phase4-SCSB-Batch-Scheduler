package org.springframework.batch.admin.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.springframework.batch.admin.service.JobService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.batch.operations.NoSuchJobException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


public class JobControllerUT extends BaseTestCase {

    @Mock
    JobController mockJobController;

    @Mock
    HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    JobParametersExtractor jobParametersExtractor;

    @Mock
    ModelMap model;

    @Mock
    JobService jobService;

    @Mock
    JobParameters jobParameters;

    @Mock
    JobExecution jobExecution;

    @Mock
    TimeZone timeZone;

    public ModelMap getModel() {
        return model;
    }

    @Before
    public void setup() {
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
    }

    @Test
    public void testgetJobName() throws Exception {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getPathInfo()).thenReturn("/jobs/PeriodicLASItemStatusReconciliation");
        Mockito.when(mockJobController.getJobName(request)).thenCallRealMethod();
        String path = mockJobController.getJobName(request);
        assertNotNull(path);
    }

    @Test
    public void testlaunch() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = null;
        String origin = "";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenReturn(Mockito.mock(JobExecution.class));
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }

    @Test
    public void testlaunch_NoSuchJobException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(NoSuchJobException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }
    @Test
    public void testlaunch_JobExecutionAlreadyRunningException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "job";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(JobExecutionAlreadyRunningException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
    }
    @Test
    public void testlaunch_JobRestartException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(JobRestartException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }
    @Test
    public void testlaunch_JobInstanceAlreadyCompleteException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(JobInstanceAlreadyCompleteException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }
    @Test
    public void testlaunch_JobParametersInvalidException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "";
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(JobParametersInvalidException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }

    @Test
    public void testDetails() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobParameters jobParameters = Mockito.mock(JobParameters.class);
        List list = new ArrayList<JobInstance>();
        list.add(jobInstance);
        Errors errors = Mockito.mock(Errors.class);
        when(jobInstance.getId()).thenReturn(1L);
        when(jobService.isLaunchable(jobName)).thenReturn(true);
        when(jobParametersExtractor.fromJobParameters(jobParameters)).thenReturn("jobParams");
        when(jobService.getLastJobParameters(jobName)).thenReturn(jobParameters);
        when(jobService.listJobInstances(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(list);
        when(jobService.getJobExecutionsForJobInstance(jobName, 1L)).thenReturn(new ArrayList<>());

        Mockito.when(mockJobController.details(model, jobName, errors, 0, 20)).thenCallRealMethod();
        String jobs = mockJobController.details(model, jobName, errors, 0, 20);
        assertNotNull(jobs);
    }

    @Test
    public void testDetails_NoSuchJobException() throws Exception {
        String jobName = "PeriodicLASItemStatusReconciliation";
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobParameters jobParameters = Mockito.mock(JobParameters.class);
        List list = new ArrayList<JobInstance>();
        list.add(jobInstance);
        Errors errors = Mockito.mock(Errors.class);
        when(jobInstance.getId()).thenReturn(1L);
        when(jobService.isLaunchable(jobName)).thenReturn(true);
        when(jobParametersExtractor.fromJobParameters(jobParameters)).thenReturn("jobParams");
        when(jobService.getLastJobParameters(jobName)).thenThrow(new NoSuchJobException());
        when(jobService.listJobInstances(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(list);
        when(jobService.getJobExecutionsForJobInstance(jobName, 1L)).thenReturn(new ArrayList<>());

        Mockito.when(mockJobController.details(model, jobName, errors, 0, 20)).thenCallRealMethod();
        String jobs = mockJobController.details(model, jobName, errors, 0, 20);
        assertNotNull(jobs);
    }


    @Test
    public void testJobs() throws Exception{
        List list=new ArrayList<String>();
        list.add("testJobs");
       when(jobService.countJobs()).thenReturn(1);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        when(jobService.listJobs(1,1)).thenReturn(list);
        when(jobService.countJobExecutionsForJob("testJobs")).thenReturn(1);
        when(jobService.isLaunchable("testJobs")).thenReturn(true);
        when(jobService.isIncrementable("testJobs")).thenReturn(true);
        Mockito.doNothing().when(mockJobController).jobs(model,0,20);
        mockJobController.jobs(model,0,20);

    }


}
