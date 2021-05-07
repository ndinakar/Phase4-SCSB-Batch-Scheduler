package org.recap.repository.jpa;

import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.ScsbConstants;
import org.recap.model.jpa.JobParamEntity;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 3/7/17.
 */
public class JobParamDetailRepositoryUT extends BaseTestCase {

    @Autowired
    JobParamDetailRepository jobParamDetailRepository;

    @Test
    public void findByJobName() throws Exception {
        JobParamEntity byJobName = jobParamDetailRepository.findByJobName(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
        assertNotNull(byJobName);
        assertNotNull(byJobName.getJobName());
        assertEquals(ScsbConstants.GENERATE_ACCESSION_REPORT_JOB, byJobName.getJobName());
    }
}