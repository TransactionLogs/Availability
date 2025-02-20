package org.anonymous.transactionlogs.steps.step1;

import org.anonymous.transactionlogs.model.ILogEntryProvider;

public class Step1Result {

	private ILogEntryProvider logEntryProvider;

	public Step1Result(){

	}

	public ILogEntryProvider getLogEntryProvider() {
		return logEntryProvider;
	}

	public void setLogEntryProvider(ILogEntryProvider logEntryProvider) {
		this.logEntryProvider = logEntryProvider;
	}
}
