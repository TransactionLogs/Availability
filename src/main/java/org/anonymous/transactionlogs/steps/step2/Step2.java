package org.anonymous.transactionlogs.steps.step2;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;

import org.anonymous.transactionlogs.model.ILogEntryProvider;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;

/**
 * Step 2 filters the log entries to retain only relevant attribute changes.
 */
public class Step2 implements IDataProcessingStep<Step2Input, Step2Result> {

	@Override
	public Step2Result execute(Step2Input input) {

		ILogEntryProvider logEntryProvider = input.getStep1Result().getLogEntryProvider();

		Set<String> semanticallyMeaningfulAttributes = input.getSemanticallyMeaningfulAttributes();

		Set<String> attributesBefore = logEntryProvider.getIncludedAttributes();
		int entriesBefore = logEntryProvider.countEntries();

		logEntryProvider.applyFilter(entry -> {
			if (!entry.getAttributeName().isPresent()) {
				// only filtering attribute-type entries
				return true;
			}
			return semanticallyMeaningfulAttributes.contains(entry.getAttributeName().get());
		});
		if (logEntryProvider.getIncludedAttributes().isEmpty()) {
			throw new IllegalStateException("No attributes included - nothing to analyze!");
		}

		Set<String> attributesAfter = logEntryProvider.getIncludedAttributes();
		int entriesAfter = logEntryProvider.countEntries();

		System.out.println(
				"Step 1 reduced " + attributesBefore.size() + " attributes to " + attributesAfter.size() + ": " + join(
						attributesBefore) + " -> " + join(attributesAfter));
		System.out.println("Log entries were reduced from " + entriesBefore + " to " + entriesAfter);

		return new Step2Result(logEntryProvider);

	}

	private String join(Collection<String> values) {
		StringJoiner joiner = new StringJoiner(";", "(", ")");
		for (String value : values) {
			joiner.add(value);
		}
		return joiner.toString();
	}

}
