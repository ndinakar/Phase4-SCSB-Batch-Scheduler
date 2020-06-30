package org.recap.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by rajeshbabuk on 29/6/17.
 */
@Service
public class DeletedRecordsExportPulService {

    @Autowired
    private RecordsExportService recordsExportService;

   
    /**
     * This method makes a rest call to scsb etl microservice to initiate the process of deleted records export for Princeton.
     *
     * @param scsbEtlUrl    the scsb etl url
     * @return status of deleted records export for PUL
     */
    public String deletedRecordsExportPul(String scsbEtlUrl, String jobName, Date createdDate, String exportStringDate) {
        return recordsExportService.exportRecords(scsbEtlUrl, jobName, createdDate, exportStringDate, "PUL");
    }
}
