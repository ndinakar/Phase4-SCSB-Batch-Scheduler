package org.recap.batch.flow;

import org.recap.PropertyKeyConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by rajeshbabuk on 5/7/17.
 */
public class MatchingAlgorithmExecutionDecider implements JobExecutionDecider {

    @Value("${" + PropertyKeyConstants.INCLUDE_MATCHING_ALGORITHM_IN_SEQUENCE_JOB + "}")
    private boolean includeMatchingAlgorithmInSequenceJob;

    /**
     * This method decides if the matching algorithm should be included in the job sequence process.
     *
     * @param jobExecution
     * @param stepExecution
     * @return FlowExecutionStatus
     */
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (includeMatchingAlgorithmInSequenceJob) {
            return FlowExecutionStatus.COMPLETED;
        } else {
            return FlowExecutionStatus.FAILED;
        }
    }
}
