package org.recap.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by rajeshbabuk on 23/6/17.
 */
@Service
public class IncrementalExportCulService  {

    @Autowired
    private RecordsExportService recordsExportService;

    /**
     * This method makes a rest call to scsb etl microservice to initiate the process of incremental export for CUL.
     *
     * @param scsbEtlUrl    the scsb etl url
     * @return sstatus of incremental export for CUL
     */
    public String incrementalExportCul(String scsbEtlUrl, String jobName, Date createdDate, String exportStringDate) {
        return recordsExportService.exportRecords(scsbEtlUrl, jobName, createdDate, exportStringDate, "CUL");
    }
}
