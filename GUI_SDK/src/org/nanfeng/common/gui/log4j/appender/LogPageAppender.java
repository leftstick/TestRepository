package org.nanfeng.common.gui.log4j.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.nanfeng.common.gui.LogPage;

public class LogPageAppender extends AppenderSkeleton {

	public static LogPageAppender appender;
	private LogPage log;

	public LogPageAppender() {
		appender = this;
	}

	public static LogPageAppender getAppender() {
		return appender;
	}

	public LogPage getLog() {
		return log;
	}

	public void setLog(LogPage log) {
		this.log = log;
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

	protected void append(final LoggingEvent event) {
		if (log != null) {
			log.append(Logger.getRootLogger().getLevel().toString() + ":"
					+ event.getMessage().toString() + "\n");
		}
	}
}
