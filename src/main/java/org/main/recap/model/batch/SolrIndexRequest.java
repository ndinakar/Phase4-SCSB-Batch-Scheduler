package org.main.recap.model.batch;

import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
public class SolrIndexRequest {
    private String processType;
    private Date createdDate;

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
