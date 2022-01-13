package org.recap.model;

import lombok.Data;


import java.util.Date;

/**
 * Created by rajeshbabuk on 5/4/17.
 */
@Data
public class ScheduleJobResponse {
    private String message;
    private Date nextRunTime;
}
