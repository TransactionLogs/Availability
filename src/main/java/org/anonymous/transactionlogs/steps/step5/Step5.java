package org.anonymous.transactionlogs.steps.step5;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.anonymous.transactionlogs.model.ILogEntryProvider;
import org.anonymous.transactionlogs.model.Itemset;
import org.anonymous.transactionlogs.model.NormalizedLogEntry;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;

/**
 * Step 5: Apply sliding window and create atomic item sets.
 */
public class Step5 implements IDataProcessingStep<Step5Input, Step5Result> {

	@Override
	public Step5Result execute(Step5Input step5Input) {

		long slidingWindowLength = step5Input.getSlidingWindowLength();

		Map<String, List<Itemset>> groupedItemsets = new HashMap<>();

		for (String partition : step5Input.getStep4Result().getPartition()) {
			ILogEntryProvider logEntryProvider = step5Input.getStep4Result().getLogEntryProvider(partition);

			// apply sliding window to group log entries that occured within the specified time fame
			List<List<NormalizedLogEntry>> groupedLogEntries = groupLogEntriesBySlidingWindow(logEntryProvider,
					slidingWindowLength);

			// check for relevant log groups and create itemsets
			List<Itemset> itemsets = groupedLogEntries.stream().filter(entryGroup -> isRelevantForAnalysis(entryGroup))
					.map(entryGroup -> new Itemset(entryGroup)).collect(Collectors.toList());

			if (itemsets.isEmpty()) {
				// no itemsets for this employee
				continue;
			}

			groupedItemsets.put(partition, itemsets);

		}

		return new Step5Result(groupedItemsets);

	}

	/**
	 * Apply the sliding to create item groups that occured in the specified time frame
	 *
	 * @param logEntryProvider    the log entries
	 * @param slidingWindowLength the length of the sliding window in ms
	 * @return the grouped log entries
	 */
	private List<List<NormalizedLogEntry>> groupLogEntriesBySlidingWindow(ILogEntryProvider logEntryProvider,
			long slidingWindowLength) {

		List<List<NormalizedLogEntry>> groupedLogEntries = new ArrayList<>();

		List<NormalizedLogEntry> eventsInSlidingWindow = new LinkedList<>();

		while (logEntryProvider.hasNext()) {

			// proceed with next event
			NormalizedLogEntry currentEvent = logEntryProvider.getNext();
			if (fitsInSlidingWindow(currentEvent, eventsInSlidingWindow, slidingWindowLength)) {
				// add event to sliding window, proceed with next event
				eventsInSlidingWindow.add(currentEvent);
				continue;
			}

			// sliding window is full, create itemset
			groupedLogEntries.add(new ArrayList(eventsInSlidingWindow));

			// then proceed sliding to the right until the current event fits in and continue
			while (!fitsInSlidingWindow(currentEvent, eventsInSlidingWindow, slidingWindowLength)) {
				eventsInSlidingWindow.remove(0);
			}

			eventsInSlidingWindow.add(currentEvent);

		}

		// create itemset for the last events in the sliding window
		if (!eventsInSlidingWindow.isEmpty()) {
			groupedLogEntries.add(new ArrayList<>(eventsInSlidingWindow));
		}

		return groupedLogEntries;

	}

	private boolean fitsInSlidingWindow(NormalizedLogEntry currentEvent, List<NormalizedLogEntry> eventsInSlidingWindow,
			long slidingWindowLength) {

		if (eventsInSlidingWindow.isEmpty()) {
			return true;
		}

		NormalizedLogEntry firstEvent = eventsInSlidingWindow.get(0);

		if (!firstEvent.getTimestamp().isBefore(currentEvent.getTimestamp()) && !Objects.equals(
				firstEvent.getTimestamp(), currentEvent.getTimestamp())) {
			// events are not in chronological order
			throw new IllegalArgumentException(
					"Compared log entries are not in chronological order: " + firstEvent + " / " + currentEvent);
		}

		return Duration.between(firstEvent.getTimestamp(), currentEvent.getTimestamp())
				.toMillis() <= slidingWindowLength;

	}

	private boolean isRelevantForAnalysis(List<NormalizedLogEntry> groupedLogEntries) {

		if (groupedLogEntries.size() < 2) {
			// no correlation possible
			return false;
		}

		boolean hasAttributeChange = false;
		boolean hasEntitlementChange = false;

		for (int i = 1; i < groupedLogEntries.size(); i++) {
			NormalizedLogEntry previous = groupedLogEntries.get(i - 1);
			NormalizedLogEntry current = groupedLogEntries.get(i);

			if (!previous.getAttributeName().equals(current.getAttributeName())) {
				hasAttributeChange = true;
			}

			if (!previous.getEntitlementName().equals(current.getEntitlementName())) {
				hasEntitlementChange = true;
			}

			if (hasAttributeChange && hasEntitlementChange) {
				return true;
			}
		}

		return false;

	}

}
