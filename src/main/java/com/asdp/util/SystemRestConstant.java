package com.asdp.util;

public class SystemRestConstant {
	public static final class UserConstant {
		private UserConstant() {
		}

		public static final String USER_CONTROLLER = "/user";
		public static final String SAVE_USER_ADDR = "/saveUser";
		public static final String FIND_USER_DETAIL_ADDR = "/findUserDetail";
		public static final String SEARCH_USER_DETAIL_ADDR = "/searchUser";
		public static final String SEARCH_HISTORY_LOGIN_ADDR = "/searchHistoryLogin";
		public static final String CHANGE_PASSWORD_ADDR = "/changePassword";
		public static final String SAVE_USER_AUTH = "SAVE_PRE_SCREENING";
	}
	
	public static final class MateriQuizConstant {
		private MateriQuizConstant() {
		}

		public static final String MATERI_QUIZ_CONTROLLER = "/materi";
		public static final String SAVE_MATERI_QUIZ_ADDR = "/saveMateri";
		public static final String FIND_MATERI_QUIZ_DETAIL_ADDR = "/findMateriDetail";
		public static final String SEARCH_MATERI_QUIZ_ADDR = "/searchMateri";
		public static final String DOWNLOAD_FILE_ADDR = "/downloadFile";
	}
}
