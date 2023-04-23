package at.redeye.SqlDBInterface.SqlDBIO;

import java.text.SimpleDateFormat;

public enum DateTimeFormat {

	SQLIF_STD_DATE_FORMAT("yyyy-MM-dd"),
	SQLIF_STD_TIME_FORMAT("HH:mm:ss"),
	SQLIF_STD_SHORTTIME_FORMAT("HH:mm"),
	SQLIF_STD_DATETIME_FORMAT("yyyy-MM-dd HH:mm:ss");

	private final String pattern;

	DateTimeFormat(String pattern) {
		this.pattern = pattern;
	}

	public SimpleDateFormat formatter() {
		return new SimpleDateFormat(pattern);
	}
}
