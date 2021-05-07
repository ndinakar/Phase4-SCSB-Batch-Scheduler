package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@Ignore
public class IncrementalExportNyplServiceUT extends BaseTestCase {

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    @Mock
    RecordsExportService recordsExportService;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testIncrementalExportNypl() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL);

    }
    @Test
    public void testIncrementalExportCul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA);

    }
    @Test
    public void testIncrementalExportPul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON)).thenCallRealMethod();
        String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON);

    }
}
