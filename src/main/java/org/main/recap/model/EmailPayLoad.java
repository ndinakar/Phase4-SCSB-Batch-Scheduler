package org.main.recap.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
public class EmailPayLoad implements Serializable {

    private String jobName;
    private Date startDate;
    private String status;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
