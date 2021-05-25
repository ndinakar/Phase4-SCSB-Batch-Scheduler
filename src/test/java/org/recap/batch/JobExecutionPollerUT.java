package org.recap.batch;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.job.JobDto;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

import java.util.*;


public class JobExecutionPollerUT extends BaseTestCase {


    JobExecutionPoller jobExecutionPoller;

    @Mock
    ScsbJobService scsbJobService;

    @Mock
    JobDto jobDto;



    @Test
    public void testJobExecutionPolle() throws Exception {
        JobExplorer jobExplorer=new JobExplorer() {
            @Override
            public List<JobInstance> getJobInstances(String jobName, int start, int count) {
                return null;
            }

            @Override
            public JobExecution getJobExecution(Long executionId) {
                return null;
            }

            @Override
            public StepExecution getStepExecution(Long jobExecutionId, Long stepExecutionId) {
                return null;
            }

            @Override
            public JobInstance getJobInstance(Long instanceId) {
                return null;
            }

            @Override
            public List<JobExecution> getJobExecutions(JobInstance jobInstance) {
                return null;
            }

            @Override
            public Set<JobExecution> findRunningJobExecutions(String jobName) {
                StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
                StepExecution execution = MetaDataInstanceFactory.createStepExecution();
                ChunkContext context = new ChunkContext(new StepContext(execution));
                Set<JobExecution> set=new HashSet<>();
                JobExecution jobexe=execution.getJobExecution();
                Date date=new Date();
                Calendar calender = Calendar.getInstance();
                calender.setTime(date);
                calender.add(Calendar.HOUR, -4);
                jobexe.setStartTime(calender.getTime());
                set.add(jobexe);
                return set;
            }

            @Override
            public List<String> getJobNames() {
                List<String> list=new ArrayList<>();
                list.add("test");
                return list;
            }

            @Override
            public List<JobInstance> findJobInstancesByJobName(String jobName, int start, int count) {
                return null;
            }

            @Override
            public int getJobInstanceCount(String jobName) throws NoSuchJobException {
                return 0;
            }
        };

        Mockito.when(scsbJobService.getJobByName("test")).thenReturn(jobDto);
        jobExecutionPoller=new JobExecutionPoller(0L,jobExplorer,null,null,scsbJobService);

    }
}
