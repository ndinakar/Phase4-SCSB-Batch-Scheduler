package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
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
    public void testDeletedRecordsExportService() throws Exception {
        String exportInstitution = ScsbCommonConstants.PRINCETON;
        String jobName = ScsbConstants.DELETED_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase());
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution)).thenCallRealMethod();
        String status=recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution);
        assertNotNull(status);
    }

}
