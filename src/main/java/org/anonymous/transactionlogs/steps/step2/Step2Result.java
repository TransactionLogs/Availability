package org.anonymous.transactionlogs.steps.step2;

import java.util.Objects;

import org.anonymous.transactionlogs.model.ILogEntryProvider;

public class Step2Result {

	private final ILogEntryProvider logEntryProvider;

	public Step2Result(ILogEntryProvider logEntryProvider) {
		this.logEntryProvider = Objects.requireNonNull(logEntryProvider);
	}

	public ILogEntryProvider getLogEntryProvider() {
		return logEntryProvider;
	}

}
