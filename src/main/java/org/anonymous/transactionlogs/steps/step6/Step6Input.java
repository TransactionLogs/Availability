package org.anonymous.transactionlogs.steps.step6;

import java.util.List;
import java.util.Objects;

import org.anonymous.transactionlogs.model.Itemset;

public class Step6Input {

	private final List<Itemset> itemsets;

	private final double minsup;

	public Step6Input(List<Itemset> itemsets, double minsup) {
		this.itemsets = Objects.requireNonNull(itemsets);
		this.minsup = minsup;
	}

	public List<Itemset> getItemsets() {
		return itemsets;
	}

	public double getMinsup() {
		return minsup;
	}

}
