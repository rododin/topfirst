/*
 * DateTimeUtils.java
 */

package com.topfirst.generic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Provides a set of useful Date/Time service methods.
 *
 * @author Rod Odin
 */
public abstract class DateTimeUtils
{
// Constants -----------------------------------------------------------------------------------------------------------

	public static final ResourceBundle DATE_TIME_BUNDLE = ResourceBundle.getBundle(DateTimeUtils.class.getPackage().getName() + ".DateTime");

	public static final String DEFAULT_DATE_PATTERN     = DATE_TIME_BUNDLE.getString("default.pattern.date"    );
	public static final String DEFAULT_TIME_PATTERN     = DATE_TIME_BUNDLE.getString("default.pattern.time"    );
	public static final String DEFAULT_DATETIME_PATTERN = DATE_TIME_BUNDLE.getString("default.pattern.datetime");

// Methods -------------------------------------------------------------------------------------------------------------

	public static String fromDate(Date date)
	{
		return fromDateTime(date, DEFAULT_DATE_PATTERN);
	}

	public static String fromTime(Date time)
	{
		return fromDateTime(time, DEFAULT_TIME_PATTERN);
	}

	public static String fromDateTime(Date datetime)
	{
		return fromDateTime(datetime, DEFAULT_DATETIME_PATTERN);
	}

	public static String fromDateTime(Date datetime, String pattern)
	{
		String rv = null;
		if (datetime != null && pattern != null)
			rv = (new SimpleDateFormat(pattern)).format(datetime);
		return rv;
	}

	public static Date toDate(String sDate) throws ParseException
	{
		return toDateTime(sDate, DEFAULT_DATE_PATTERN);
	}

	public static Date toTime(String sTime) throws ParseException
	{
		return toDateTime(sTime, DEFAULT_TIME_PATTERN);
	}

	public static Date toDateTime(String sDateTime) throws ParseException
	{
		return toDateTime(sDateTime, DEFAULT_DATETIME_PATTERN);
	}

	public static Date toDateTime(String sDateTime, String pattern) throws ParseException
	{
		Date rv = null;
		if (sDateTime != null && sDateTime.trim().length() != 0 && pattern != null)
			rv = (new SimpleDateFormat(pattern)).parse(sDateTime);
		return rv;
	}
}
