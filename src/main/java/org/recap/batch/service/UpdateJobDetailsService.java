package org.recap.batch.service;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.recap.model.job.JobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by rajeshbabuk on 12/4/17.
 */
@Service
public class UpdateJobDetailsService {

    @Autowired
    private ScsbJobService scsbJobService;

    /**
     * This method makes a rest call to solr client microservice to update the job with next execution time.
     *
     * @param solrClientUrl    the solr client url
     * @param jobName          the job name
     * @param lastExecutedTime the last executed time
     * @return status of updating the job
     */
    public String updateJob(String solrClientUrl, String jobName, Date lastExecutedTime, Long jobInstanceId) throws Exception {
        JobDto jobDto = scsbJobService.getJobByName(jobName);
        jobDto.setLastExecutedTime(lastExecutedTime);
        jobDto.setJobInstanceId(jobInstanceId.intValue());
        if (StringUtils.isNotBlank(jobDto.getCronExpression())) {
            CronExpression cronExpression = new CronExpression(jobDto.getCronExpression());
            jobDto.setNextRunTime(cronExpression.getNextValidTimeAfter(lastExecutedTime));
        }
        return scsbJobService.updateJob(jobDto);
    }
}
