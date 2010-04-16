package au.com.langdale.webserver;

import org.mortbay.log.Logger;

import au.com.langdale.util.SimpleLogger;

public class Slf4jLog extends SimpleLogger implements Logger {
	private static final long serialVersionUID = 6219923713692482982L;

	public Slf4jLog() {
		this("org.mortbay.log");
	}

	public Slf4jLog(String name) {
		super(name);
	}

	public Logger getLogger(String name) {
		return this;
	}

	@Override
	public void setDebugEnabled(boolean enabled) {
		// ignored
	}
}
