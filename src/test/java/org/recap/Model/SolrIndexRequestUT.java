package org.recap.Model;

import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.ScheduleJobResponse;
import org.recap.model.batch.SolrIndexRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class SolrIndexRequestUT extends BaseTestCase
{
    @Test
    public void SolrIndexRequest() throws Exception
    {
        String processType = "test";
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
         solrIndexRequest.setProcessType(processType);
         solrIndexRequest.setCreatedDate(dNow);
        assertNotNull(solrIndexRequest.getProcessType());
        assertNotNull(solrIndexRequest.getCreatedDate());
    }
}
