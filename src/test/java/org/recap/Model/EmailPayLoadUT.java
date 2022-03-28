package org.recap.Model;

import lombok.Data;
import lombok.Setter;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.EmailPayLoad;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;


@Data
public class EmailPayLoadUT extends BaseTestCase {

    @Test
    public void  EmailPayLoad() {
        String jobname = "test";
        String jobDescription = "test";
        String jobAction = "test";
        String status = "test";
        String message = "test";
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobname);
        emailPayLoad.setJobAction(jobAction);
        emailPayLoad.setStartDate(dNow);
        emailPayLoad.setStatus(status);
        emailPayLoad.setMessage(message);
        emailPayLoad.setJobDescription(jobDescription);

        assertNotNull(emailPayLoad.getJobAction());
        assertNotNull(emailPayLoad.getMessage());
        assertNotNull(emailPayLoad.getJobDescription());
        assertNotNull(emailPayLoad.getStartDate());
        assertNotNull(emailPayLoad.getJobName());
        assertNotNull(emailPayLoad.getStatus());


    }
}