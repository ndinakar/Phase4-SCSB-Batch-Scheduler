package org.recap.batch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
@Ignore
@RunWith(PowerMockRunner.class)
public class DeletedRecordsExportServiceUT {

    @Mock
    RecordsExportService recordsExportService;

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    private String scsbEtlUrl;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testDeletedRecordsExportPulService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, ScsbCommonConstants.PRINCETON)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, ScsbCommonConstants.PRINCETON)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate, ScsbCommonConstants.PRINCETON);
        assertNotNull(status);
    }

    @Test
    public void testDeletedRecordsExportCulService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA);
        assertNotNull(status);
    }
    @Test
    public void testDeletedRecordsExportNyplService() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,ScsbCommonConstants.NYPL);
        assertNotNull(status);
    }

}
