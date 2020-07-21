package org.recap.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by rajeshbabuk on 23/6/17.
 */
@Service
public class IncrementalExportPulService {

    @Autowired
    private RecordsExportService recordsExportService;

    public String incrementalExportPul(String scsbEtlUrl, String jobName, Date createdDate, String exportStringDate) {
       return recordsExportService.exportRecords(scsbEtlUrl, jobName, createdDate, exportStringDate, "PUL");
    }
}
