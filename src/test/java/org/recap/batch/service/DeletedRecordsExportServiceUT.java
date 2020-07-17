package org.recap.batch.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class DeletedRecordsExportServiceUT {

    @Mock
    DeletedRecordsExportPulService mockdeletedRecordsExportPulService;

    @Mock
    DeletedRecordsExportCulService mockdeletedRecordsExportCulService;

    @Mock
    DeletedRecordsExportNyplService mockdeletedRecordsExportNyplService;

    @Mock
    RecordsExportService recordsExportService;

    @Value("${scsb.etl.url}")
    private String scsbEtlUrl;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testdeletedRecordsExportPulService() throws Exception {
        ReflectionTestUtils.setField(mockdeletedRecordsExportPulService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate,"PUL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(mockdeletedRecordsExportPulService.deletedRecordsExportPul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate)).thenCallRealMethod();
        String status=mockdeletedRecordsExportPulService.deletedRecordsExportPul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL,createdDate,exportStringDate);
        assertNotNull(status);
    }

    @Test
    public void testdeletedRecordsExportCulService() throws Exception {
        ReflectionTestUtils.setField(mockdeletedRecordsExportCulService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate,"CUL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(mockdeletedRecordsExportCulService.deletedRecordsExportCul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate)).thenCallRealMethod();
        String status=mockdeletedRecordsExportCulService.deletedRecordsExportCul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL,createdDate,exportStringDate);
        assertNotNull(status);
    }
    @Test
    public void testdeletedRecordsExportNyplService() throws Exception {
        ReflectionTestUtils.setField(mockdeletedRecordsExportNyplService,"recordsExportService",recordsExportService);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate,"NYPL")).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(mockdeletedRecordsExportNyplService.deletedRecordsExportNypl(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate)).thenCallRealMethod();
        String status=mockdeletedRecordsExportNyplService.deletedRecordsExportNypl(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL,createdDate,exportStringDate);
        assertNotNull(status);
    }

}
