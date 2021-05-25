package org.recap.util;

import org.apache.commons.collections.CollectionUtils;
import org.recap.ScsbConstants;
import org.recap.batch.service.ScsbJobService;
import org.recap.model.job.JobParamDataDto;
import org.recap.model.job.JobParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeshbabuk on 29/6/17.
 */
@Service
public class JobDataParameterUtil {

    @Autowired
    private ScsbJobService scsbJobService;

    /**
     * This method builds the job parameters from the database and builds a map.
     *
     * @param jobName the job name
     * @return the map
     */
    public Map<String, String> buildJobRequestParameterMap(String jobName) {
        Map<String, String> parameterMap = new HashMap<>();
        JobParamDto jobParamDto = scsbJobService.getJobParamsByJobName(jobName);
        if (CollectionUtils.isNotEmpty(jobParamDto.getJobParamDataDtos())) {
            for (JobParamDataDto jobParamDataDto : jobParamDto.getJobParamDataDtos()) {
                parameterMap.put(jobParamDataDto.getParamName(), jobParamDataDto.getParamValue());
            }
        }
        return parameterMap;
    }

    /**
     * This method returns the date string in format "yyyy-MM-dd HH:mm".
     *
     * @param createdDate the created date
     * @return date format string for export
     */
    public String getDateFormatStringForExport(Date createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ScsbConstants.EXPORT_DATE_FORMAT);
        return dateFormatter.format(getFromDate(createdDate));
    }

    /**
     * This method builds the from date using the current date whose time would be 00:00:00
     *
     * @param createdDate the created date
     * @return the from date
     */
    public Date getFromDate(Date createdDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
}
