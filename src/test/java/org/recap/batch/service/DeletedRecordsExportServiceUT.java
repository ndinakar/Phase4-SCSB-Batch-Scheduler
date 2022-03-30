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
import org.recap.util.JobDataParameterUtil;
import org.recap.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertNotNull;


@RunWith(PowerMockRunner.class)

public class DeletedRecordsExportServiceUT {

    @Mock
    RecordsExportService recordsExportService;

    @Mock
    CommonService commonService;

    @Mock
    JobDataParameterUtil jobDataParameterUtil;

    @Mock
    PropertyUtil propertyUtil;

    @Mock
    RestTemplate restTemplate;



    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    private String scsbEtlUrl;

    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Test
    public void testDeletedRecordsExportService() throws Exception {
        ReflectionTestUtils.setField(recordsExportService,"commonService",commonService);
        ReflectionTestUtils.setField(recordsExportService,"jobDataParameterUtil",jobDataParameterUtil);
        ReflectionTestUtils.setField(recordsExportService,"propertyUtil",propertyUtil);
        Mockito.when(recordsExportService.commonService.getRestTemplate()).thenReturn(restTemplate);

        String exportInstitution = ScsbCommonConstants.PRINCETON;
        String jobName = ScsbConstants.DELETED_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase());
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution)).thenCallRealMethod();
     //   String status=recordsExportService.exportRecords(scsbEtlUrl, jobName,createdDate,exportStringDate, exportInstitution);
      //  assertNotNull(status);
    }

}
