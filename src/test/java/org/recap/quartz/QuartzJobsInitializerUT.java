package org.recap.quartz;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;
import org.recap.BaseTestCaseUT;
import org.recap.RecapConstants;
import org.recap.model.jpa.JobEntity;
import org.recap.repository.jpa.JobDetailsRepository;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by rajeshbabuk on 20/4/17.
 */
public class QuartzJobsInitializerUT extends BaseTestCaseUT {

    @InjectMocks
    QuartzJobsInitializer mockquartzJobsInitializer;

    @Mock
    JobDetailsRepository jobDetailsRepository;

    @Mock
    JobEntity jobEntity;

    @Mock
    JobLauncher jobLauncher;

    @Mock
    JobLocator jobLocator;

    @Mock
    Scheduler scheduler;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mockquartzJobsInitializer,"jobLocator",jobLocator);
        ReflectionTestUtils.setField(mockquartzJobsInitializer,"scheduler",scheduler);
        ReflectionTestUtils.setField(mockquartzJobsInitializer,"jobLauncher",jobLauncher);
        ReflectionTestUtils.setField(mockquartzJobsInitializer,"jobDetailsRepository",jobDetailsRepository);
        List<JobEntity> jobEntities=new ArrayList<>();
        jobEntities.add(jobEntity);
        Mockito.when(jobEntity.getCronExpression()).thenReturn("* * * * * ? *");
        Mockito.when(jobDetailsRepository.findAll()).thenReturn(jobEntities);
    }

   @Test
    public void testInitializeJobsUnscheduled() {
        Mockito.when(jobEntity.getStatus()).thenReturn(RecapConstants.UNSCHEDULED);
        Mockito.when(jobEntity.getJobName()).thenReturn(RecapConstants.UNSCHEDULED);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }


    @Test
    public void testInitializeJobsScheduled() {
        Mockito.when(jobEntity.getStatus()).thenReturn(RecapConstants.SCHEDULE);
        Mockito.when(jobEntity.getJobName()).thenReturn(RecapConstants.SCHEDULE);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }

    @Test
    public void testInitializeJobsException() throws Exception {
        Mockito.when(jobEntity.getStatus()).thenReturn(RecapConstants.SCHEDULE);
        Mockito.when(jobEntity.getJobName()).thenReturn(RecapConstants.SCHEDULE);
        Mockito.when(scheduler.scheduleJob(Mockito.any(),Mockito.any())).thenThrow(NullPointerException.class);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }
}
