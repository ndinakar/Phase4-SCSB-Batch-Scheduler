package org.recap.batch.service;

import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Created by angelind on 17/5/17.
 */
@Service
public class AccessionService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to solr client microservice to initiate the accession process.
     *
     * @param solrClientUrl     the solr client url
     * @return status of the accession process
     */
    public String processAccession(String solrClientUrl) {
        return commonService.executeService(solrClientUrl,  RecapConstants.ACCESSION_URL, HttpMethod.GET);
    }

}
