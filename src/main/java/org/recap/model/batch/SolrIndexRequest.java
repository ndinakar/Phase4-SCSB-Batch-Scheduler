package org.recap.model.batch;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
@Getter
@Setter
public class SolrIndexRequest {
    private String processType;
    private Date createdDate;
}
