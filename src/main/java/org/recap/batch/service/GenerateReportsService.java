package org.recap.batch.service;

import org.recap.RecapConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.recap.util.JobDataParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by angelind on 2/5/17.
 */
@Service
public class GenerateReportsService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateReportsService.class);

    @Autowired
    JobDataParameterUtil jobDataParameterUtil;

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to solr client microservice to generate report for given process type.
     *
     * @param solrClientUrl     the solr client url
     * @param reportCreatedDate the report created date
     * @param jobName           the job name
     * @return status of the generated report
     */
    public String generateReport(String solrClientUrl, Date reportCreatedDate, String jobName) {
        SolrIndexRequest solrIndexRequest = getSolrIndexRequest(reportCreatedDate, jobName);
        return commonService.getResponse(solrIndexRequest, solrClientUrl, RecapConstants.GENERATE_REPORT_URL);

    }

    /**
     * Gets solr index request.
     *
     * @param reportCreatedDate the report created date
     * @param reportType        the report type
     * @return the solr index request
     */
    public SolrIndexRequest getSolrIndexRequest(Date reportCreatedDate, String reportType) {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(reportType);
        solrIndexRequest.setCreatedDate(jobDataParameterUtil.getFromDate(reportCreatedDate));
        return solrIndexRequest;
    }


}
