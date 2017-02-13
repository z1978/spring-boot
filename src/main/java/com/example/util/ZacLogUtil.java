package com.example.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * log4jを使用する為のUtil
 */
public class ZacLogUtil {
	static Log log = LogFactory.getLog(ZacLogUtil.class);

	private ZacLogUtil() {
	}

	public static Log getLog() {
		return log;
	}
}
