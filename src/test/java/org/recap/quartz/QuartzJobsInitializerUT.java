package org.recap.quartz;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.job.JobDto;
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
    ScsbJobService scsbJobService;
    
    @Mock
    JobDto jobDto;

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
        ReflectionTestUtils.setField(mockquartzJobsInitializer,"scsbJobService",scsbJobService);
        List<JobDto> jobDtos=new ArrayList<>();
        jobDtos.add(jobDto);
        Mockito.when(jobDto.getCronExpression()).thenReturn("* * * * * ? *");
        Mockito.when(scsbJobService.getAllJobs()).thenReturn(jobDtos);
    }

   @Test
    public void testInitializeJobsUnscheduled() {
        Mockito.when(jobDto.getStatus()).thenReturn(ScsbConstants.UNSCHEDULED);
        Mockito.when(jobDto.getJobName()).thenReturn(ScsbConstants.UNSCHEDULED);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }


    @Test
    public void testInitializeJobsScheduled() {
        Mockito.when(jobDto.getStatus()).thenReturn(ScsbConstants.SCHEDULE);
        Mockito.when(jobDto.getJobName()).thenReturn(ScsbConstants.SCHEDULE);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }

    @Test
    public void testInitializeJobsException() throws Exception {
        Mockito.when(jobDto.getStatus()).thenReturn(ScsbConstants.SCHEDULE);
        Mockito.when(jobDto.getJobName()).thenReturn(ScsbConstants.SCHEDULE);
        Mockito.when(scheduler.scheduleJob(Mockito.any(),Mockito.any())).thenThrow(NullPointerException.class);
        mockquartzJobsInitializer.initializeJobs();
        assertTrue(true);
    }
}
