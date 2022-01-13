package org.recap.model;

import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
@Data
public class EmailPayLoad implements Serializable {
    private String jobName;
    private String jobDescription;
    private String jobAction;
    private Date startDate;
    private String status;
    private String message;
}
