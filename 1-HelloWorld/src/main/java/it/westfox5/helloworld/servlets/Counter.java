package it.westfox5.helloworld.servlets;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;

public class Counter {
	private static final String ATTRIBUTE = "counter";

	public static Counter of(ServletContext context) {
		Counter c = (Counter) context.getAttribute(ATTRIBUTE);
		if (c == null)
			context.setAttribute(ATTRIBUTE, new Counter());

		return (Counter) context.getAttribute(ATTRIBUTE);
	}

	private final Map<String, Long> _counters;

	private Counter() {
		this._counters = new HashMap<String, Long>();
	}

	public long getAndIncrement(String sessionId) {
		synchronized (_counters) {
			_counters.put(sessionId, (_counters.getOrDefault(sessionId, 0L) + 1));
		}
		return _counters.get(sessionId);
	}
}
