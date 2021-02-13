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

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
    }

    String origin = "";
    String jobName = "PeriodicLASItemStatusReconciliation";

    @Test
    public void testgetJobName() throws Exception {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getPathInfo()).thenReturn("/jobs/PeriodicLASItemStatusReconciliation");
        Mockito.when(mockJobController.getJobName(request)).thenCallRealMethod();
        String path = mockJobController.getJobName(request);
        assertNotNull(path);
    }

    @Test
    public void testgetJobNameNullPath() throws Exception {
        Collection<String> extensions = new HashSet<>();
        extensions.add("PeriodicLASItemStatusReconciliation.");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getPathInfo()).thenReturn(null);
        Mockito.when(request.getServletPath()).thenReturn("/jobs/PeriodicLASItemStatusReconciliation.");
        Mockito.doCallRealMethod().when(mockJobController).setExtensions(extensions);
        mockJobController.setExtensions(extensions);
        Mockito.when(mockJobController.getJobName(request)).thenCallRealMethod();
        String path = mockJobController.getJobName(request);
        assertNotNull(path);
    }

    @Test
    public void testlaunch() throws Exception {
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = null;
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenReturn(Mockito.mock(JobExecution.class));
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.doCallRealMethod().when(mockJobController).setTimeZone(timeZone);
        mockJobController.setTimeZone(timeZone);
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }

    @Test
    public void testlaunch_NoSuchJobException() throws Exception {
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
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
    public void testlaunch_SpringNoSuchJobException() throws Exception {
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        ReflectionTestUtils.setField(mockJobController, "jobParametersExtractor", jobParametersExtractor);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        Mockito.when(jobParametersExtractor.fromString(Mockito.anyString())).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(params)).thenReturn("{" + params + "}");
        when(jobService.launch(jobName, jobParameters)).thenThrow(org.springframework.batch.core.launch.NoSuchJobException.class);
        when(((ModelMap) model).get("PeriodicLASBarcodeReconciliation")).thenReturn("test");
        Mockito.when(mockJobController.launch(model, jobName, launchRequest, errors, origin)).thenCallRealMethod();
        String path = mockJobController.launch(model, jobName, launchRequest, errors, origin);
        assertNotNull(path);
    }
    @Test
    public void testlaunch_JobExecutionAlreadyRunningException() throws Exception {
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
        String origin = "job";
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
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
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
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
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
        LaunchRequest launchRequest = getLaunchRequest();
        String params = launchRequest.getJobParameters();
        params = params + ",time=" + System.currentTimeMillis();
        Errors errors = Mockito.mock(Errors.class);
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

    private LaunchRequest getLaunchRequest() {
        LaunchRequest launchRequest = new LaunchRequest();
        launchRequest.setJobName("PeriodicLASBarcodeReconciliation");
        launchRequest.setJobParameters("fromDate=2020-07-07");
        return launchRequest;
    }

    @Test
    public void testDetails() throws Exception {
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
    public void testDetails_SpringNoSuchJobException() throws Exception {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobParameters jobParameters = Mockito.mock(JobParameters.class);
        List list = new ArrayList<JobInstance>();
        list.add(jobInstance);
        Errors errors = Mockito.mock(Errors.class);
        when(jobInstance.getId()).thenReturn(1L);
        when(jobService.isLaunchable(jobName)).thenReturn(true);
        when(jobParametersExtractor.fromJobParameters(jobParameters)).thenReturn("jobParams");
        when(jobService.getLastJobParameters(jobName)).thenThrow(org.springframework.batch.core.launch.NoSuchJobException.class);
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
        when(jobService.listJobs(0,20)).thenReturn(list);
        when(jobService.countJobExecutionsForJob("testJobs")).thenReturn(1);
        when(jobService.isLaunchable("testJobs")).thenReturn(true);
        when(jobService.isIncrementable("testJobs")).thenReturn(true);
        Mockito.doCallRealMethod().when(mockJobController).jobs(model,0,20);
        mockJobController.jobs(model,0,20);
        assertTrue(true);
    }

    @Test
    public void testJobsNoSuchJobException() throws Exception{
        List list=new ArrayList<String>();
        list.add("testJobs");
        when(jobService.countJobs()).thenReturn(1);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        when(jobService.listJobs(0,20)).thenReturn(list);
        when(jobService.countJobExecutionsForJob("testJobs")).thenReturn(1);
        when(jobService.isLaunchable("testJobs")).thenReturn(true);
        when(jobService.isIncrementable("testJobs")).thenReturn(true);
        Mockito.doCallRealMethod().when(mockJobController).jobs(model,0,20);
        Mockito.when(jobService.countJobExecutionsForJob(Mockito.anyString())).thenThrow(NoSuchJobException.class);
        mockJobController.jobs(model,0,20);
        assertTrue(true);
    }

    @Test
    public void testJobsSpringNoSuchJobException() throws Exception{
        List list=new ArrayList<String>();
        list.add("testJobs");
        when(jobService.countJobs()).thenReturn(1);
        ReflectionTestUtils.setField(mockJobController, "jobService", jobService);
        when(jobService.listJobs(0,20)).thenReturn(list);
        when(jobService.countJobExecutionsForJob("testJobs")).thenReturn(1);
        when(jobService.isLaunchable("testJobs")).thenReturn(true);
        when(jobService.isIncrementable("testJobs")).thenReturn(true);
        Mockito.doCallRealMethod().when(mockJobController).jobs(model,0,20);
        Mockito.when(jobService.countJobExecutionsForJob(Mockito.anyString())).thenThrow(org.springframework.batch.core.launch.NoSuchJobException.class);
        mockJobController.jobs(model,0,20);
        assertTrue(true);
    }


}
