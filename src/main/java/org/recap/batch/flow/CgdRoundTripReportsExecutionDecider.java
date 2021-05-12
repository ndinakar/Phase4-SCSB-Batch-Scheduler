package org.recap.batch.flow;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by rajeshbabuk on 12/May/2021
 */
public class CgdRoundTripReportsExecutionDecider implements JobExecutionDecider {

    @Value("${include.cgd.round.trip.reports.in.sequence.job}")
    private boolean includeCgdRoundTripReportsInSequenceJob;

    /**
     * This method decides if the CGD Round Trip Reports should be included in the job sequence process.
     *
     * @param jobExecution
     * @param stepExecution
     * @return FlowExecutionStatus
     */
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (includeCgdRoundTripReportsInSequenceJob) {
            return FlowExecutionStatus.COMPLETED;
        } else {
            return FlowExecutionStatus.FAILED;
        }
    }
}
