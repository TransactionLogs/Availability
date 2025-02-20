package org.anonymous.transactionlogs.steps.step3;

import java.util.List;
import java.util.stream.Collectors;

import org.anonymous.transactionlogs.model.ILogEntryProvider;

public class Step3Result {

	private final ILogEntryProvider logEntryProvider;

	public Step3Result(ILogEntryProvider logEntryProvider) {
		this.logEntryProvider = logEntryProvider;
	}

	public ILogEntryProvider getLogEntryProvider() {
		return logEntryProvider;
	}

	public List<String> getIncludedEntitlements() {
		return logEntryProvider.getIncludedEntitlements().stream().sorted().collect(Collectors.toList());
	}

	public synchronized Step3Result cloneAndFilter(String entitlement) {
		Step3Result cloned = new Step3Result(logEntryProvider.clone());
		cloned.getLogEntryProvider().applyFilter(entry -> {
			if (!entry.getEntitlementName().isPresent()) {
				// only filtering entitlement-type entries
				return true;
			}
			return entry.getEntitlementName().get().equals(entitlement);
		});
		return cloned;
	}

}
