package org.anonymous.transactionlogs.steps.step4;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.anonymous.transactionlogs.model.ILogEntryProvider;

public class Step4Result {

	private final Map<String, ILogEntryProvider> partitionedLogEntryProviders;

	public Step4Result(Map<String, ILogEntryProvider> logEntryProviders) {
		this.partitionedLogEntryProviders = Objects.requireNonNull(logEntryProviders);
	}

	public List<String> getPartition() {
		return partitionedLogEntryProviders.keySet().stream().sorted().collect(Collectors.toList());
	}

	public ILogEntryProvider getLogEntryProvider(String employee) {
		return partitionedLogEntryProviders.get(employee);
	}

}
