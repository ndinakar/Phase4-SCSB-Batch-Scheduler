package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.util.JobDataParameterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncrementalExportNyplServiceUT extends BaseTestCase {

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    @Mock
    RecordsExportService recordsExportService;

    @Mock
    IncrementalExportNyplService incrementalExportNyplService;

    @Mock
    IncrementalExportCulService incrementalExportCulService;

    @Mock
    IncrementalExportPulService incrementalExportPulService;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testIncrementalExportNypl() throws Exception {
        ReflectionTestUtils.setField(incrementalExportNyplService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,"NYPL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportNyplService.incrementalExportNypl(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_NYPL,createdDate,exportStringDate)).thenCallRealMethod();
        String status = incrementalExportNyplService.incrementalExportNypl(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_NYPL,createdDate,exportStringDate);

    }
    @Test
    public void testIncrementalExportCul() throws Exception {
        ReflectionTestUtils.setField(incrementalExportCulService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,"CUL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportCulService.incrementalExportCul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_CUL,createdDate,exportStringDate)).thenCallRealMethod();
        String status = incrementalExportCulService.incrementalExportCul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_CUL,createdDate,exportStringDate);

    }
    @Test
    public void testIncrementalExportPul() throws Exception {
        ReflectionTestUtils.setField(incrementalExportPulService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl,RecapConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,"PUL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportPulService.incrementalExportPul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_PUL,createdDate,exportStringDate)).thenCallRealMethod();
        String status = incrementalExportPulService.incrementalExportPul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_PUL,createdDate,exportStringDate);

    }
}
