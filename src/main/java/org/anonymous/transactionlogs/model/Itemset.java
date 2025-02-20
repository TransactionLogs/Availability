package org.anonymous.transactionlogs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Itemset {

	private final String uid;

	private List<NormalizedLogEntry> logEntries;

	public Itemset(List<NormalizedLogEntry> logEntries) {
		this.uid = UUID.randomUUID().toString();
		this.logEntries = Collections.unmodifiableList(new ArrayList<>(logEntries));
	}

	public Itemset(String uid, List<NormalizedLogEntry> logEntries) {
		this.uid = Objects.requireNonNull(uid);
		this.logEntries = Collections.unmodifiableList(new ArrayList<>(logEntries));
	}

	public List<NormalizedLogEntry> getLogEntries() {
		return logEntries;
	}

	public String getUid() {
		return uid;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("ItemSet '");
		sb.append(uid);
		sb.append("':\n");
		Iterator<NormalizedLogEntry> it = logEntries.iterator();
		while(it.hasNext()){
			sb.append(it.next().toString());
			if(it.hasNext()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
