package org.main.recap;

/**
 * Created by rajeshbabuk on 7/4/17.
 */
public class RecapConstants {

    public static final String SUCCESS = "Success";
    public static final String API_KEY = "api_key";
    public static final String RECAP = "recap";
    public static final String SCHEDULE = "Schedule";
    public static final String RESCHEDULE = "Reschedule";
    public static final String UNSCHEDULE = "Unschedule";
    public static final String JOB_NAME = "jobName";
    public static final String JOB_LAUNCHER = "jobLauncher";
    public static final String JOB_LOCATOR = "jobLocator";
    public static final String TRIGGER_SUFFIX = "_Trigger";
    public static final String ERROR_INVALID_CRON_EXPRESSION = "Cron expression is invalid.";
    public static final String ERROR_JOB_FAILED_SCHEDULING = "Job failed to be scheduled.";
    public static final String ERROR_JOB_FAILED_RESCHEDULING = "Job failed to be rescheduled.";
    public static final String ERROR_JOB_FAILED_UNSCHEDULING = "Job failed to be unscheduled.";
    public static final String JOB_SUCCESS_SCHEDULING = "Job successfully scheduled.";
    public static final String JOB_SUCCESS_RESCHEDULING = "Job successfully rescheduled.";
    public static final String JOB_SUCCESS_UNSCHEDULING = "Job successfully unscheduled.";
    public static final String LOG_ERROR = "error-->";
    public static final String ONGOING_MATCHING_ALGORITHM_JOB = "ongoingMatchingAlgorithmJob";
    public static final String GENERATE_ACCESSION_REPORT_JOB = "GenerateAccessionReport";
    public static final String PURGE_EMAIL_URL = "purge/purgeEmailAddress";
    public static final String PURGE_EXCEPTION_REQUEST_URL = "purge/purgeExceptionRequests";
    public static final String PURGE_ACCESSION_REQUEST_URL = "purge/purgeAccessionRequests";
    public static final String MATCHING_ALGORITHM_URL = "ongoingMatchingAlgorithmService/ongoingMatchingAlgorithmJob";
    public static final String GENERATE_REPORT_URL = "generateReportService/generateReports";
    public static final String REPORT_DELETED_RECORDS_URL = "reportDeleted/records";


    public static final String ACCESSION_URL = "sharedCollection/ongoingAccessionJob";
    public static final String BATCH_JOB_EMAIL_URL = "batchJobEmailService/batchJobEmail";
    public static final String UPDATE_JOB_URL = "updateJobService/updateJob";
    public static final String DAILY_RECONCILATION_URL = "/dailyReconcilation/startDailyReconcilation";
    public static final String ACCESSION_RECOCILATION_URL = "/accessionReconcilation/startAccessionReconcilation";
    public static final String PURGE_EXCEPTION_REQUESTS = "PurgeExceptionRequests";
    public static final String PURGE_EMAIL_ADDRESS = "PurgeEmailAddress";
    public static final String MATCHING_ALGORITHM = "MatchingAlgorithm";
    public static final String DAILY_RECONCILIATION = "DailyReconcilation";
    public static final String GENERATE_ACCESSION_REPORT = "GenerateAccessionReport";
    public static final String ACCESSION = "Accession";
    public static final String RUN_JOB_SEQUENTIALLY = "RunJobSequentially";
    public static final String PURGE_ACCESSION_REQUESTS = "PurgeAccessionRequests";

    private RecapConstants(){}
}
