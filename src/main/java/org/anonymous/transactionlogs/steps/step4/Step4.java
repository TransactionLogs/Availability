package org.anonymous.transactionlogs.steps.step4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anonymous.transactionlogs.model.ILogEntryProvider;
import org.anonymous.transactionlogs.model.NormalizedLogEntry;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;
import org.anonymous.transactionlogs.steps.step1.LogEntryProviderImpl;

/**
 * Step 4 partitions the log entries by digital identity. For each partition a new log entry provider is created.
 */
public class Step4 implements IDataProcessingStep<Step4Input, Step4Result> {

	@Override
	public Step4Result execute(Step4Input step4Input) {

		Map<String, List<NormalizedLogEntry>> groupedEntries = new HashMap<>();

		ILogEntryProvider logEntryProvider = step4Input.getStep3Result().getLogEntryProvider();

		while (logEntryProvider.hasNext()) {
			NormalizedLogEntry logEntry = logEntryProvider.getNext();
			String employee = logEntry.getDigitalIdentity();
			groupedEntries.computeIfAbsent(employee, _employee -> new ArrayList<>()).add(logEntry);
		}

		Map<String, ILogEntryProvider> groupedLogEntryProviders = new HashMap<>();
		groupedEntries.forEach((employee, entries) -> {
			groupedLogEntryProviders.put(employee, new LogEntryProviderImpl(entries));
		});

		return new Step4Result(groupedLogEntryProviders);

	}

}
