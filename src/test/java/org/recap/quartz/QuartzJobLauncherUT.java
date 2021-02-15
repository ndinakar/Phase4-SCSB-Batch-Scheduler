package org.recap.quartz;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.recap.BaseTestCaseUT;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QuartzJobLauncherUT extends BaseTestCaseUT {

    @InjectMocks
    QuartzJobLauncher quartzJobLauncher;

    @Mock
    JobExecutionContext context;

    @Mock
    JobLauncher jobLauncher;

    @Mock
    JobLocator jobLocator;

    @Mock
    Job job;

    @Mock
    JobExecution jobExecution;

    @Test
    public void testInitializeJobs() throws JobExecutionException, NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        quartzJobLauncher.setJobLauncher(jobLauncher);
        quartzJobLauncher.setJobLocator(jobLocator);
        quartzJobLauncher.setJobName("jobName");
        Mockito.when(jobLocator.getJob("jobName")).thenReturn(job);
        Mockito.when(jobLauncher.run(Mockito.any(),Mockito.any())).thenReturn(jobExecution);
        Mockito.when(jobExecution.getId()).thenReturn(1l);
        Mockito.when(job.getName()).thenReturn("name");
        quartzJobLauncher.executeInternal(context);
        assertNotNull(quartzJobLauncher.getJobLauncher());
        assertNotNull(quartzJobLauncher.getJobName());
        assertNotNull(quartzJobLauncher.getJobLocator());
    }

    @Test
    public void testInitializeJobsUnscheduled() throws JobExecutionException, NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Mockito.when(jobLocator.getJob("jobName")).thenThrow(NullPointerException.class);
        Mockito.when(jobLauncher.run(Mockito.any(),Mockito.any())).thenThrow(NullPointerException.class);
        quartzJobLauncher.executeInternal(context);
        assertNotNull(quartzJobLauncher.getJobLauncher());
        assertNull(quartzJobLauncher.getJobName());
        assertNotNull(quartzJobLauncher.getJobLocator());
    }
}
