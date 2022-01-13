package org.recap.model;

import lombok.Data;


/**
 * Created by rajeshbabuk on 5/4/17.
 */

@Data
public class ScheduleJobRequest {
    private Integer jobId;
    private String jobName;
    private String cronExpression;
    private String scheduleType;
}
