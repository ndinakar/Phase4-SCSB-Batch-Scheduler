package org.recap.model.batch;

import lombok.Data;


import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
@Data
public class SolrIndexRequest {
    private String processType;
    private Date createdDate;
}
