package org.main.recap;

/**
 * Created by rajeshbabuk on 7/4/17.
 */
public class RecapConstants {

    /**
     * The constant SUCCESS.
     */
    public static final String SUCCESS = "Success";
    /**
     * The constant API_KEY.
     */
    public static final String API_KEY = "api_key";
    /**
     * The constant RECAP.
     */
    public static final String RECAP = "recap";
    /**
     * The constant SCHEDULE.
     */
    public static final String SCHEDULE = "Schedule";
    /**
     * The constant RESCHEDULE.
     */
    public static final String RESCHEDULE = "Reschedule";
    /**
     * The constant UNSCHEDULE.
     */
    public static final String UNSCHEDULE = "Unschedule";
    /**
     * The constant JOB_NAME.
     */
    public static final String JOB_NAME = "jobName";
    /**
     * The constant JOB_LAUNCHER.
     */
    public static final String JOB_LAUNCHER = "jobLauncher";
    /**
     * The constant JOB_LOCATOR.
     */
    public static final String JOB_LOCATOR = "jobLocator";
    /**
     * The constant TRIGGER_SUFFIX.
     */
    public static final String TRIGGER_SUFFIX = "_Trigger";
    /**
     * The constant ERROR_INVALID_CRON_EXPRESSION.
     */
    public static final String ERROR_INVALID_CRON_EXPRESSION = "Cron expression is invalid.";
    /**
     * The constant ERROR_JOB_FAILED_SCHEDULING.
     */
    public static final String ERROR_JOB_FAILED_SCHEDULING = "Job failed to be scheduled.";
    /**
     * The constant ERROR_JOB_FAILED_RESCHEDULING.
     */
    public static final String ERROR_JOB_FAILED_RESCHEDULING = "Job failed to be rescheduled.";
    /**
     * The constant ERROR_JOB_FAILED_UNSCHEDULING.
     */
    public static final String ERROR_JOB_FAILED_UNSCHEDULING = "Job failed to be unscheduled.";
    /**
     * The constant JOB_SUCCESS_SCHEDULING.
     */
    public static final String JOB_SUCCESS_SCHEDULING = "Job successfully scheduled.";
    /**
     * The constant JOB_SUCCESS_RESCHEDULING.
     */
    public static final String JOB_SUCCESS_RESCHEDULING = "Job successfully rescheduled.";
    /**
     * The constant JOB_SUCCESS_UNSCHEDULING.
     */
    public static final String JOB_SUCCESS_UNSCHEDULING = "Job successfully unscheduled.";
    /**
     * The constant LOG_ERROR.
     */
    public static final String LOG_ERROR = "error-->";
    /**
     * The constant ONGOING_MATCHING_ALGORITHM_JOB.
     */
    public static final String ONGOING_MATCHING_ALGORITHM_JOB = "ongoingMatchingAlgorithmJob";
    /**
     * The constant GENERATE_ACCESSION_REPORT_JOB.
     */
    public static final String GENERATE_ACCESSION_REPORT_JOB = "GenerateAccessionReport";

    /**
     * The constant PURGE_EMAIL_URL.
     */
    public static final String PURGE_EMAIL_URL = "purge/purgeEmailAddress";
    /**
     * The constant PURGE_EXCEPTION_REQUEST_URL.
     */
    public static final String PURGE_EXCEPTION_REQUEST_URL = "purge/purgeExceptionRequests";
    /**
     * The constant PURGE_ACCESSION_REQUEST_URL.
     */
    public static final String PURGE_ACCESSION_REQUEST_URL = "purge/purgeAccessionRequests";
    /**
     * The constant MATCHING_ALGORITHM_URL.
     */
    public static final String MATCHING_ALGORITHM_URL = "ongoingMatchingAlgorithmService/ongoingMatchingAlgorithmJob";
    /**
     * The constant GENERATE_REPORT_URL.
     */
    public static final String GENERATE_REPORT_URL = "generateReportService/generateReports";
    /**
     * The constant ACCESSION_URL.
     */
    public static final String ACCESSION_URL = "sharedCollection/ongoingAccessionJob";
    /**
     * The constant BATCH_JOB_EMAIL_URL.
     */
    public static final String BATCH_JOB_EMAIL_URL = "batchJobEmailService/batchJobEmail";
    /**
     * The constant UPDATE_JOB_URL.
     */
    public static final String UPDATE_JOB_URL = "updateJobService/updateJob";
    /**
     * The constant DAILY_RECONCILATION_URL.
     */
    public static final String DAILY_RECONCILATION_URL = "/dailyReconcilation/startDailyReconcilation";
    /**
     * The constant ACCESSION_RECOCILATION_URL.
     */
    public static final String ACCESSION_RECOCILATION_URL = "/accessionReconcilation/startAccessionReconcilation";

    /**
     * The constant PURGE_EXCEPTION_REQUESTS.
     */
    public static final String PURGE_EXCEPTION_REQUESTS = "PurgeExceptionRequests";
    /**
     * The constant PURGE_EMAIL_ADDRESS.
     */
    public static final String PURGE_EMAIL_ADDRESS = "PurgeEmailAddress";
    /**
     * The constant MATCHING_ALGORITHM.
     */
    public static final String MATCHING_ALGORITHM = "MatchingAlgorithm";
    /**
     * The constant DAILY_RECONCILIATION.
     */
    public static final String DAILY_RECONCILIATION = "DailyReconcilation";
    /**
     * The constant GENERATE_ACCESSION_REPORT.
     */
    public static final String GENERATE_ACCESSION_REPORT = "GenerateAccessionReport";
    /**
     * The constant ACCESSION.
     */
    public static final String ACCESSION = "Accession";
    /**
     * The constant RUN_JOB_SEQUENTIALLY.
     */
    public static final String RUN_JOB_SEQUENTIALLY = "RunJobSequentially";
    /**
     * The constant PURGE_ACCESSION_REQUESTS.
     */
    public static final String PURGE_ACCESSION_REQUESTS = "PurgeAccessionRequests";

    private RecapConstants(){}
}
