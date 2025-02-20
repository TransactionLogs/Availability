package org.anonymous.transactionlogs.steps.step5;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.anonymous.transactionlogs.model.Itemset;

public class Step5Result {

	private final Map<String, List<Itemset>> itemsets;

	public Step5Result(Map<String, List<Itemset>> itemsets) {
		this.itemsets = Objects.requireNonNull(itemsets);
	}

	public List<String> getPartitions() {
		return itemsets.keySet().stream().sorted().collect(java.util.stream.Collectors.toList());
	}

	public List<Itemset> getItemsets(String partition) {
		return itemsets.get(partition);
	}

	public boolean isEmpty() {
		return itemsets.isEmpty();
	}

	public int countItemsets() {
		return itemsets.values().stream().mapToInt(List::size).sum();
	}

}
