package org.anonymous.transactionlogs.model;

import java.util.Set;
import java.util.function.Function;

public interface ILogEntryProvider {

	/**
	 * Filter the event entries to retain only relevant ones
	 *
	 * @param filter Function that determines whether a log entry is relevant
	 */
	void applyFilter(Function<NormalizedLogEntry, Boolean> filter);

	/**
	 * @return true if the provider has more entries
	 */
	boolean hasNext();

	/**
	 * @return the next entry
	 */
	NormalizedLogEntry getNext();

	/**
	 * @return Set of all attributes present in the log entries
	 */
	Set<String> getIncludedAttributes();

	/**
	 * @return Set of all entitlements present in the log entries
	 */
	Set<String> getIncludedEntitlements();

	/**
	 * @return A new log entry provide that is a clone of this one. Modifications to one of the two will not affect the other one.
	 */
	ILogEntryProvider clone();

	/**
	 * @return the number of entries available by this provider
	 */
	int countEntries();

}
