package org.recap.model.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rajeshbabuk on 4/4/17.
 */
@Entity
@Table(name = "JOB_T", schema = "recap", catalog = "recap")
@AttributeOverride(name = "id", column = @Column(name = "JOB_ID"))
@Getter
@Setter
public class JobEntity extends AbstractEntity<Integer> {

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_DESC")
    private String jobDescription;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_EXECUTED_TIME")
    private Date lastExecutedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NEXT_RUN_TIME")
    private Date nextRunTime;

    @Column(name = "CRON_EXP")
    private String cronExpression;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "JOB_INSTANCE_ID")
    private Integer jobInstanceId;

}
