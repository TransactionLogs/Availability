package org.anonymous.transactionlogs.steps.step3;

import java.util.Set;

import org.anonymous.transactionlogs.model.ILogEntryProvider;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;
import org.anonymous.transactionlogs.steps.step3.Step3Input;
import org.anonymous.transactionlogs.steps.step3.Step3Result;

/**
 * Step 3 filters the log entries to retain only relevant entitlement changes.
 */
public class Step3 implements IDataProcessingStep<Step3Input, Step3Result> {

	@Override
	public Step3Result execute(Step3Input input) {

		ILogEntryProvider logEntryProvider = input.getStep2Result().getLogEntryProvider();

		Set<String> relevantEntitlements = input.getRelevantEntitlements().orElse(null);
		if (relevantEntitlements == null) {
			// not filter applied -> analyze them all!
			return new Step3Result(logEntryProvider);
		}

		Set<String> entitementsBefore = logEntryProvider.getIncludedEntitlements();
		int entriesBefore = logEntryProvider.countEntries();

		logEntryProvider.applyFilter(entry -> {
			if (!entry.getEntitlementName().isPresent()) {
				// only filtering entitlement-type entries
				return true;
			}
			return relevantEntitlements.contains(entry.getEntitlementName().get());
		});
		if (logEntryProvider.getIncludedEntitlements().isEmpty()) {
			throw new IllegalStateException("No entitlements included - nothing to analyze!");
		}

		Set<String> entitlementsAfter = logEntryProvider.getIncludedEntitlements();
		int entriesAfter = logEntryProvider.countEntries();

		System.out.println(
				"Step 1 reduced " + entitementsBefore.size() + " entitlements to " + entitlementsAfter.size() + ".");
		System.out.println("Log entries were reduced from " + entriesBefore + " to " + entriesAfter);

		return new Step3Result(logEntryProvider);

	}

}
