package com.asdp.util;

import java.util.HashMap;
import java.util.Map;

public class SystemConstant {
	private SystemConstant() {}

	/*** New Relic Constant ***/
	public static final String NEW_RELIC_ENVIRONMENT = "newrelic_environment";

	/*** Field Definition Constant ***/
	public static final String FIELD_DEFINITION_REFRENCE = "Reference";

	/*** Date time format ***/
	public static final String SYSTEM_DATE_DAY = "dd";
	public static final String SYSTEM_DATE_MONTH = "MM";
	public static final String SYSTEM_DATE_MONTH_NAME = "MMM";
	public static final String SYSTEM_DATE_YEAR = "yyyy";
	public static final String SYSTEM_DATE_YEAR_SHORT = "yy";
	public static final String SYSTEM_DATE_MASK = "dd-MM-yyyy";
	public static final String SYSTEM_DATE_MONTH_DETAIL = "dd-MMM-yyyy";
	public static final String SYSTEM_DATE_MONTH_DETAIL_2 = "dd MMM yyyy";
	public static final String SYSTEM_DATE_MONTH_DETAIL_SHORT = "dd-M-yyyy";
	public static final String SYSTEM_DATE_TIME_MASK = "dd-MMM-yyyy HH:mm";
	public static final String SYSTEM_DATE_TIME_SECOND_MASK = "dd-MMM-yyyy HH:mm:ss";
	public static final String SYSTEM_MONTH_YEAR_MASK = "MM-yyyy";
	public static final String SYSTEM_YEAR_MONTH_MASK = "yyyy-MM";
	public static final String SYSTEM_FULL_MONTH_YEAR_MASK = "MMMMM yyyy";
	public static final String SYSTEM_FULL_MONTH_FULL_DATE_MASK = "dd MMMMM yyyy";
	public static final String SYSTEM_TIME_MASK = "dd-MM-yyyy HH:mm:ss";
	public static final String SYSTEM_FULL_TIME_MASK = "dd MMMMM yyyy HH:mm:ss";
	public static final String SYSTEM_FULL_TIME_MINUTE_MASK = "dd-MMMMM-yyyy HH:mm";
	public static final String SYSTEM_TIME_MASK_SECONDHAND = "dd-MM-yyyy HH:mm";
	public static final String HOUR_MINUTE_MASK = "HH:mm";
	public static final String HOUR_MINUTE_SECOND_MASK = "HH:mm:ss";
	public static final String HOUR_MINUTE_SECOND_MASK_NO_DELIMITER = "HHmmss";
	public static final String HOUR_MINUTE_MILISECOND_MASK_NO_DELIMITER = "HHmmssSSS";
	public static final String SYSTEM_DATE_MASK_NO_DELIMITER = "yyyyMMdd";
	public static final String SYSTEM_DATE_MASK_YEAR_MONTH = "yyyyMM";
	public static final String HOUR_MINUTE_MASK_NO_DELIMITER = "HHmm";
	public static final String DATABASE_DATE_FORMAT_STD = "MM/dd/yyyy";
	public static final String HOUR_MINUTE_SECOND_AMPM = "hh:mm:ss a";
	public static final String SYSTEM_FULL_DATE = "dd MM yyyy";
	public static final String SYSTEM_FULL_DATE_NO_DELIMITER = "ddMMyyyy";
	public static final String SYSTEM_REPORT_DATE = "yyyyMMdd_HHmm";
	public static final String SYSTEM_REPORT_DATE_TIME = "yyyyMMdd_HHmmssSSS";
	public static final String SYSTEM_REPORT_MONTH = "MMMMMMMMMM";
	public static final String SYSTEM_DATE_TIME_NO_DELIMITER = "yyyyMMddhhmmss";
	public static final String SYSTEM_TIME_MASK_SECONDHAND_24HOUR = "dd-MM-yyyy kk:mm";
	public static final String SYSTEM_DATE_TIME_FULL_NO_DELIMITER = "yyyyMMddhhmmssms";

	public static final String IPLUS_DECIMAL_FORMAT = "#,##0.########";

	public static final String WEB_SERVICE_DATETIME = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String WEB_SERVICE_DATE = "yyyy-MM-dd";
	public static final String WEB_SERVICE_TIME = "HH:mm:ss.SSS";
	public static final String SYSTEM_DATE_MASK_2 = "dd/MM/yyyy";
	public static final String SYSTEM_DATE_TIME_MASK_2 = "dd/MM/yyyy HH:mm:ss";
	public static final String WEB_SERVICE_DATETIME_MASK_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public static final String SYSTEM_DATE_TIME_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";

	public static final String EXPORT_DATETIME = "yyyy-MM-dd.HH-mm-ss-SSS";

	public static final String FIF_DATE = "dd/MM/yyyy";

	/*** Currency format ***/
	public static final String SYSTEM_CURRENCY_MASK = "#,##0.00";
	public static final String DECIMAL_MASK = "###0.00";
	public static final String SYSTEM_NUMBER_MASK = "#,##0";

	public static final String CLOSE_ON_HOLIDAY = "TIDAK";
	public static final String OPEN_ON_HOLIDAY = "YA";

	public static final String PATH_SEPARATOR = "/";
	public static final String PATH_SEPARATOR_ENCODE = "%2F";
	public static final String COLON = ":";
	public static final String COLON_ENCODE = "%3A";
	public static final String OPEN_BRACKET = "[";
	public static final String OPEN_BRACKET_ENCODE = "%5B";
	public static final String CLOSE_BRACKET = "]";
	public static final String CLOSE_BRACKET_ENCODE = "%5D";
	public static final String COMA = ",";
	public static final String COMA_ENCODE = "%2C";

	public static final String SEPARATOR_FILE_NAME = "__";
	public static final String PIPE_SEPARATOR = "|";
	public static final String SEMI_COLON_DELIMITER = ";";
	public static final String COMMA_DELIMITER = ",";
	public static final String USER_OBJECT_KEY = "user";


	public static final String SYSTEM_USER = "SYSTEM";

	public static final String YES = "YES";
	public static final String Y = "Y";
	public static final String NO = "NO";
	public static final String N = "N";

	public static final String MAIL_SEPERATOR = ";";
	public static final String EMAIL_DELIMITER = ";";
	public static final String REPORT_DELIMETER_NEW_LINE = "_#N";
	public static final String PERCENTAGE_HTML_NUMBER = "&#37;";
	public static final String PERCENTAGE_STRING = "%";

	public static final String ERROR_LIST = "errorList";
	public static final String ERROR_CODE = "ERROR_CODE";

	public static final String VIOLATION_EXCEPTION_MESSAGES = "could not execute batch";

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String FAILED = "failed";
	public static final String EMPTY = "empty";
	public static final String ACCEPTED = "accepted";
	public static final String ACCEPT_PENDING = "accept-pending";
	public static final String REJECTED = "rejected";
	public static final String PASSED = "passed";
	public static final String PENDING = "pending";
	public static final String INITIAL = "initial";
	public static final String BRANCHED = "branched";

	public static final String WILDCARD = "%";

	public static final String TRANSACTION_ID = "transaction_id";
	public static final String STREAM_ID = "stream_id".concat("_").concat(TRANSACTION_ID);

	/*** Config Service Key ***/
	public static final String LOGIN_HOST = "login.host";
	public static final String COMMON_HOST = "common.host";
	public static final String PICKUP_HOST = "pickup.host";
	public static final String RAC_HOST = "rac.host";
	public static final String PRE_SCEENING_HOST = "pre.screening.host";
	public static final String MONITORING_HOST = "monitoring.host";
	public static final String RETRY_CONFIG = "retry.config";
	public static final String STREAM_CHUNK = "stream.chunk";
	public static final String CUST_PORTFOLIO_WOW = "cust_portfolio_wow";
	public static final String FETCH_SIZE = "fetch.size";
	public static final String CREDIT_LIMIT_HOST = "credit.limit.host";
	public static final String SENDTOHOST_HOST = "sendtohost.host";
	public static final String GATEWAY_HOST = "gateway.host";
	public static final String CREDIT_APPROVAL_HOST = "credit.approval.host";
	public static final String UNDERWRITING_HOST = "underwriting.host";
	public static final String INTERVIEW_HOST = "interview.host";
	public static final String JOIN_AND_HOST = "join.and.host";
	public static final String JOIN_OR_HOST = "join.or.host";
	public static final String SPLIT_HOST = "split.host";
	public static final String IF_STAGE_HOST = "if.stage.host";
	public static final String OAUTH_REQ_URL = "oauth.request.url";
	public static final String CREDIT_SCORING_HOST = "credit.scoring.host";
	public static final String ELIGIBLE_CUSTOMER_HOST = "eligible.customer.host";
	public static final String LOAN_TYPE_HOST = "loan.type.host";
	/*** Api Gateway Needs ***/
	public static final String BTPN_API_KEY = "BTPN-ApiKey";
	public static final String BTPN_TIMESTAMP = "BTPN-Timestamp";
	public static final String BTPN_SIGNATURE = "BTPN-Signature";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BASIC = "Basic";
	public static final String GRANT_TYPE	= "grant_type";
	public static final String CLIENT_CREDENTIALS	= "client_credentials";
	public static final String GATEWAY_LIMITATION = "GATEWAY_LIMITATION";
	public static final String MAXIMUM_RETRY = "MAXIMUM_RETRY";
	public static final String SIKP_IDENTIFIER = "/sikp/calon";
	public static final String SIKP_PLAFON = "/sikp/plafon";
	public static final String USSD_URL = "ussd.url";
	public static final String USSD_USER = "ussd.user";
	public static final String USSD_PASS = "ussd.pass";
	public static final String USSD_TEXT = "ussd.text";

	/*** Expired Day Needs ***/
	public static final String EXPIRED_DAY_RANGE_VALUE = "EXPIRED_DAY_RANGE_VALUE";
	public static final String EXPIRED_CHECK = "EXPIRED_CHECK";
	public static final String RETAILER_EXPIRED_DAY_RANGE_VALUE = "RETAILER_EXPIRED_DAY_RANGE_VALUE";
	public static final String RETAILER_EXPIRED_CHECK = "RETAILER_EXPIRED_CHECK";

	/*** pickup files needs ***/
	public static final String RELEASE_PENDING_AFTER = "release.pending.after";

	/*** Eligible Customer ***/
	public static final String MANDATORY_FIELD = "error.field.mandatory";
	public static final String INVALID_PHONE_NUMBER = "error.is.not.number";
	public static final String INVALID_DATE = "error.is.not.valid.date";
	public static final String CRN_IS_NOT_FOUND = "error.crn.is.not.found";
	public static final String MSISDN_IS_NOT_FOUND = "error.msisdn.is.not.found";
	public static final String MOBILE_PHONE = "label.phone";
	public static final String DOB = "label.dob";
	public static final String FULL_NAME = "label.full.name";
	public static final String GE01_CODE = "GE01";
	public static final String GE02_CODE = "GE01";
	public static final String NEW_ELIGIBLE = "NEW";
	public static final String EXIST_ELIGIBLE = "EXIST";
	public static final String DATA_NOT_FOUND = "error.data.is.not.found";

	public static final class ValidFlag {
		private ValidFlag() {}
		public static final int INVALID = 0;
		public static final int VALID = 1;
		public static final String INVALID_STR = "Invalid";
		public static final String VALID_STR = "Valid";
		public static final boolean TRUE_BOOL = true;
		public static final Map<Integer, String> validFlagMap = new HashMap<>();

		static {
			validFlagMap.put(Integer.valueOf(INVALID), INVALID_STR);
			validFlagMap.put(Integer.valueOf(VALID), VALID_STR);
		}
	}

	public static final class stateDefinition {
		private stateDefinition() {}

		/*** Definition Type ***/
		public static final String LOAN_CHANNEL_DEF = "LOAN_CHANNEL_DEF";
		public static final String DATA_FORMAT_DEF = "DATA_FORMAT_DEF";
		public static final String DATA_SOURCE_DEF = "DATA_SOURCE_DEF";
		public static final String CRITERIA_DEF = "CRITERIA_DEF";
		public static final String PRE_SCREENING_DEF = "PRE_SCREENING_DEF";
		public static final String CREDIT_LIMIT_DEF = "CREDIT_LIMIT_DEF";
		public static final String NOTIFICATION_DEF = "NOTIFICATION_DEF";
		public static final String SEND_TO_HOST_DEF = "SEND_TO_HOST_DEF";
		public static final String CREDIT_APPROVAL_DEF = "CREDIT_APPROVAL_DEF";
		public static final String UNDERWRITING_DEF = "UNDERWRITING_DEF";
		public static final String INTERVIEW_DEF = "INTERVIEW_DEF";
		public static final String SPLIT_DEF1 = "SPLIT_DEF1";
		public static final String SPLIT_DEF2 = "SPLIT_DEF2";
		public static final String IF_STAGE_DEF = "IF_STAGE_DEF";
		public static final String JOIN_AND_DEF = "JOIN_AND_DEF";
		public static final String CREDIT_SCORING_DEF = "CREDIT_SCORING_DEF";
		public static final String LOAN_TYPE_DEF = "LOAN_TYPE_DEF";

		/*** Stages Code ***/
		public static final String STAGE_DATA_CAPTURE = "DC";
		public static final String STAGE_PRE_SCREENING = "PS";
		public static final String STAGE_UNDERWRITING = "UW";
		public static final String STAGE_VERIFICATION = "VF";
		public static final String STAGE_CREDIT_SCORING = "CS";
		public static final String STAGE_CREDIT_LIMIT = "CL";
		public static final String STAGE_CREDIT_REVIEW = "CR";
		public static final String STAGE_SEND_TO_HOST = "STH";
		public static final String STAGE_NOTIFICATION = "NTFY";
		public static final String STAGE_CREDIT_APPROVAL = "CA";
		public static final String STAGE_INTERVIEW = "ITVW";
		public static final String STAGE_JOIN_AND = "JAND";
		public static final String STAGE_JOIN_OR = "JOR";
		public static final String STAGE_SPLIT = "SP";
		public static final String STAGE_IF = "IF";
		public static final String STAGE_LOAN_TYPE = "LTYP";
	}

	public static final class UploadConstants {
		public static final String EXT_PDF = ".pdf";

		public static final String PATH = "document";
		public static final String URL_PREVIEW = "http://178.128.216.198:8081";
		public static final String VALID_SUFFIX 	= "-valid";
		public static final String INVALID_SUFFIX	= "-invalid";
		public static final String PROCESSED_SUFFIX 	= "-processed";
		public static final String UNPROCESSED_SUFFIX	= "-unprocessed";

		public static final String KEY_VALIDATION_RESULT = "validationResult";
		public static final String KEY_VALIDATION_RESULT_DESCR = "validationResultDescr";
		public static final String KEY_STAT_CHECK_INTERVAL = "statusCheckInterval";
		public static final String KEY_STAT_CHECK_ID = "statusCheckID";
		public static final String KEY_VALID_RECORDS = "keyValidRecords";
		public static final String KEY_INVALID_RECORDS = "keyInvalidRecords";
		public static final String KEY_FILENAME = "keyFilename";
		public static final String KEY_VALIDATED_DATETIME = "keyValidatedDateTime";
		public static final String KEY_PREVIOUS_EXIST = "keyPreviousExist";
		public static final String KEY_PROCESS_RESULT = "processResult";
		public static final String KEY_PROCESS_RESULT_DESCR = "processResultDescr";
		public static final String KEY_PROCESSED_RECORDS = "keyProcessedRecords";
		public static final String KEY_UNPROCESSED_RECORDS = "keyUnprocessedRecords";
		public static final String KEY_PROCESSED_DATETIME = "keyProcessedDateTime";

		public static final Long DEFAULT_STAT_CHECK_INTERVAL = 10000L;

		public static final Integer STAT_WAIT_FOR_VALIDATION = 0;
		public static final Integer STAT_VALIDATING = 1;
		public static final Integer STAT_VALIDATED = 2;
		public static final Integer STAT_FAIL_TO_VALIDATE = 3;

		public static final Integer STAT_WAIT_FOR_PROCESSING = 0;
		public static final Integer STAT_PROCESSING = 1;
		public static final Integer STAT_PROCESSED = 2;
		public static final Integer STAT_FAIL_TO_PROCESS = 3;

		public static final String STAT_WAIT_FOR_VALIDATION_DESCR = "Wait for validation";
		public static final String STAT_VALIDATING_DESCR = "Validating";
		public static final String STAT_VALIDATED_DESCR = "Validated";
		public static final String STAT_FAIL_TO_VALIDATE_DESCR = "Fail to validate";
		public static final String STAT_WAIT_FOR_PROCESSING_DESCR = "Wait for processing";
		public static final String STAT_PROCESSING_DESCR = "Processing";
		public static final String STAT_PROCESSED_DESCR = "Processed";
		public static final String STAT_FAIL_TO_PROCESS_DESCR = "Fail to process";

		public static final Map<Integer, String> UPLOAD_STATUS_MAPPING = new HashMap<>();
		static{
			UPLOAD_STATUS_MAPPING.put(STAT_WAIT_FOR_VALIDATION, STAT_WAIT_FOR_VALIDATION_DESCR);
			UPLOAD_STATUS_MAPPING.put(STAT_VALIDATING, STAT_VALIDATING_DESCR);
			UPLOAD_STATUS_MAPPING.put(STAT_VALIDATED, STAT_VALIDATED_DESCR);
			UPLOAD_STATUS_MAPPING.put(STAT_FAIL_TO_VALIDATE, STAT_FAIL_TO_VALIDATE_DESCR);
		}

		public static final Map<Integer, String> PROCESS_STATUS_MAPPING = new HashMap<>();
		static{
			PROCESS_STATUS_MAPPING.put(STAT_WAIT_FOR_PROCESSING, STAT_WAIT_FOR_PROCESSING_DESCR);
			PROCESS_STATUS_MAPPING.put(STAT_PROCESSING, STAT_PROCESSING_DESCR);
			PROCESS_STATUS_MAPPING.put(STAT_PROCESSED, STAT_PROCESSED_DESCR);
			PROCESS_STATUS_MAPPING.put(STAT_FAIL_TO_PROCESS, STAT_FAIL_TO_PROCESS_DESCR);
		}
	}
	
	public static final class UserRoleConstants{
		public static final String SUPERADMIN = "SUPERADMIN";
		public static final String ADMIN = "ADMIN";
		public static final String USER = "USER";
		public static final String VIEWER = "VIEWER";
	}
	
	public static final class StatusConstants{
		public static final String EXPIRED = "EXPIRED";
		public static final String ACTIVE = "ACTIVE";
		public static final String PENDING = "PENDING";
	}

}
