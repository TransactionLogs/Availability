package org.anonymous.transactionlogs.steps.step1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.anonymous.transactionlogs.model.ILogEntryProvider;
import org.anonymous.transactionlogs.model.NormalizedLogEntry;

public class LogEntryProviderImpl implements ILogEntryProvider {

	private List<NormalizedLogEntry> entries;

	private Integer index = null;

	public LogEntryProviderImpl(List<NormalizedLogEntry> entries) {
		this.entries = Objects.requireNonNull(entries);
	}

	@Override
	public void applyFilter(Function<NormalizedLogEntry, Boolean> filter) {

		if (index != null) {
			throw new UnsupportedOperationException("Cannot apply filter after returning entries.");
		}

		Iterator<NormalizedLogEntry> iterator = entries.iterator();
		while (iterator.hasNext()) {
			NormalizedLogEntry entry = iterator.next();
			if (!filter.apply(entry)) {
				iterator.remove();
			}
		}

	}

	@Override
	public boolean hasNext() {
		if (index == null) {
			index = 0;
		}
		return index < entries.size();
	}

	@Override
	public NormalizedLogEntry getNext() {
		if (!hasNext()) {
			throw new UnsupportedOperationException("No entries left.");
		}
		return entries.get(index++);
	}

	@Override
	public Set<String> getIncludedAttributes() {
		return entries.stream().filter(entry -> entry.getAttributeName().isPresent())
				.map(entry -> entry.getAttributeName().get()).collect(Collectors.toSet());
	}

	@Override
	public int countEntries() {
		return entries.size();
	}

	@Override
	public Set<String> getIncludedEntitlements() {
		return entries.stream().filter(entry -> entry.getEntitlementName().isPresent())
				.map(entry -> entry.getEntitlementName().get()).collect(Collectors.toSet());
	}

	@Override
	public ILogEntryProvider clone() {
		return new LogEntryProviderImpl(new ArrayList<>(entries));
	}

}
