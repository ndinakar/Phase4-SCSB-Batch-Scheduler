package org.recap.batch.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCase;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.util.JobDataParameterUtil;
import org.recap.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncrementalExportNyplServiceUT extends BaseTestCase {

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    private String scsbEtlUrl;

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



    Date createdDate = new Date(System.currentTimeMillis());
    String exportStringDate= "2020-07-07";

    @Before
    public  void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(recordsExportService,"commonService",commonService);
        ReflectionTestUtils.setField(recordsExportService,"jobDataParameterUtil",jobDataParameterUtil);
        ReflectionTestUtils.setField(recordsExportService,"propertyUtil",propertyUtil);
        Mockito.when(recordsExportService.commonService.getRestTemplate()).thenReturn(restTemplate);

    }


    @Test
    public void testIncrementalExportNypl() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL)).thenCallRealMethod();
       // String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.NYPL);

    }
    @Test
    public void testIncrementalExportCul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA)).thenCallRealMethod();
      //  String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.COLUMBIA);

    }
    @Test
    public void testIncrementalExportPul() throws Exception {
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON)).thenCallRealMethod();
      //  String status = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DATA_EXPORT_ETL_URL,createdDate,exportStringDate,ScsbCommonConstants.PRINCETON);

    }
}
