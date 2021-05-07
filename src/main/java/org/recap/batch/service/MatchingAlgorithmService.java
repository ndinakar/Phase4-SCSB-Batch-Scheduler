package org.recap.batch.service;

import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.batch.SolrIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
@Service
public class MatchingAlgorithmService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to solr client microservice to initiate the matching algorithm process.
     *
     * @param solrClientUrl  the solr client url
     * @param createdDate    the created date
     * @return status of the matching algorithm process.
     */
    public String initiateMatchingAlgorithm(String solrClientUrl, Date createdDate) {
        SolrIndexRequest solrIndexRequest = getSolrIndexRequest(createdDate);
        return commonService.getResponse(solrIndexRequest, solrClientUrl, ScsbConstants.MATCHING_ALGORITHM_URL, HttpMethod.POST);
    }

    /**
     * Gets solr index request.
     *
     * @param createdDate the created date
     * @return the solr index request
     */
    public SolrIndexRequest getSolrIndexRequest(Date createdDate) {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        solrIndexRequest.setCreatedDate(createdDate);
        return solrIndexRequest;
    }
}
