/*
 * Copyright(c) 2018 Hemajoo Ltd.
 * ---------------------------------------------------------------------------
 * This file is part of the Hemajoo's Foundation project which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 * 
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * ---------------------------------------------------------------------------
 */
package com.hemajoo.foundation.common.log4j.appender;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

/**
 * A dated log4j file appender.
 * <p>
 * This appender appends the date and time information to the log file name.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
public class DatedFileAppender extends FileAppender
{
	/**
	 * Default date pattern in case it's not defined in the log4j configuration file.
	 */
	@SuppressWarnings("nls")
	private String datePattern = "yyyyMMdd_HHmmss";

	@Override
	@SuppressWarnings("nls")
	public void setFile(String strFile)
	{
		String strDate;
		SimpleDateFormat sdf;

		if (datePattern!=null && strFile!=null)
		{
			sdf = new SimpleDateFormat(datePattern);
			strDate = sdf.format(new Date());
			fileName = strFile.replaceAll("%date%", strDate);
		}
		else
		{
			System.err.println("Either File or DatePattern options are not set for appender [" + name + "].");
		}
	}

	/**
	 * Returns the date pattern.
	 * <p>
	 * @return Date pattern.
	 */
	public String getDatePattern()
	{
		return datePattern;
	}

	/**
	 * Sets the date pattern.
	 * <p>
	 * @param datePattern Date pattern.
	 */
	public void setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
	}
}