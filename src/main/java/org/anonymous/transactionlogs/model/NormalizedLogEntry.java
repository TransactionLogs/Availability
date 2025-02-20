package org.anonymous.transactionlogs.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class NormalizedLogEntry implements Comparable<NormalizedLogEntry> {

	private final LogEntryType type;

	private final LocalDateTime timestamp;

	private final String digitalIdentity;

	private final String value;

	private String attributeName;

	private String entitlementName;

	private ThreadLocal<DateTimeFormatter> dateFormatter = ThreadLocal.withInitial(
			() -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

	public NormalizedLogEntry(LogEntryType type, LocalDateTime timestamp, String digitalIdentity, String value) {
		this.type = Objects.requireNonNull(type);
		this.timestamp = Objects.requireNonNull(timestamp);
		this.digitalIdentity = Objects.requireNonNull(digitalIdentity);
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(timestamp.format(dateFormatter.get()));
		sb.append(" ");
		sb.append(type.name());
		sb.append(": '");
		sb.append(digitalIdentity);
		sb.append("' - '");
		sb.append(value);
		sb.append("'");
		return sb.toString();
	}

	public Optional<String> getAttributeName() {
		return Optional.ofNullable(attributeName);
	}

	public NormalizedLogEntry withAttributeName(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}

	public Optional<String> getEntitlementName() {
		return Optional.ofNullable(entitlementName);
	}

	public NormalizedLogEntry withEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
		return this;
	}

	@Override
	public int compareTo(NormalizedLogEntry o) {
		return timestamp.compareTo(o.timestamp);
	}

	public LogEntryType getType() {
		return type;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getDigitalIdentity() {
		return digitalIdentity;
	}

	public String getValue() {
		return value;
	}

	public ThreadLocal<DateTimeFormatter> getDateFormatter() {
		return dateFormatter;
	}
}
