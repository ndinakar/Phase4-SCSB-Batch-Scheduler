package org.recap.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by rajeshbabuk on 5/4/17.
 */
@Getter
@Setter
public class ScheduleJobResponse {
    private String message;
    private Date nextRunTime;
}
