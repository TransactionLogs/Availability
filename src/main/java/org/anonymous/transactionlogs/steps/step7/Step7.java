package org.anonymous.transactionlogs.steps.step7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.anonymous.transactionlogs.model.AssociationRuleWrapper;
import org.anonymous.transactionlogs.model.Itemset;
import org.anonymous.transactionlogs.model.NormalizedLogEntry;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;
import org.anonymous.transactionlogs.steps.step6.Step6Result;

import apriori4j.AnalysisResult;
import apriori4j.AprioriAlgorithm;
import apriori4j.AssociationRule;
import apriori4j.Transaction;

/**
 * Step 7 evaluates the frequent item sets to generate association rules. Due to the utilized Apriori library, we cannot separate the frequent item set mining from the association rule generation. This means that the frequent item set generation is also executed in this step. We still retain step 6 for conceptual clarity.
 */
public class Step7 implements IDataProcessingStep<Step7Input, Step7Result> {

	private List<Transaction> transactions;

	@Override
	public Step7Result execute(Step7Input step7Input) {

		double minsup = step7Input.getStep6Result().getStep6Input().getMinsup();
		double minconf = step7Input.getMinconf();

		this.transactions = new ArrayList<>();
		List<Itemset> itemsets = step7Input.getStep6Result().getStep6Input().getItemsets();
		// convert to transaction as expected by apriori4j
		for (Itemset itemset : itemsets) {
			Set<String> transactionValues = new HashSet<>();
			for (NormalizedLogEntry logEntry : itemset.getLogEntries()) {
				StringBuilder sb = new StringBuilder(logEntry.getType().name());
				sb.append(":");
				sb.append(logEntry.getValue());
				transactionValues.add(sb.toString());
			}
			transactions.add(new Transaction(transactionValues));
		}

		try {
			AnalysisResult analysisResult = new AprioriAlgorithm(minsup, minconf).analyze(transactions);

			List<AssociationRuleWrapper> wrappedAssociationRules = analysisResult.getAssociationRules().stream()
					.map(rule -> wrap(rule, step7Input)).collect(Collectors.toList());

			return new Step7Result(step7Input, analysisResult, wrappedAssociationRules);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private AssociationRuleWrapper wrap(AssociationRule rule, Step7Input step7Input) {
		int frequency = calculateFrequency(rule, step7Input.getStep6Result());
		return new AssociationRuleWrapper(rule, frequency);
	}

	private int calculateFrequency(AssociationRule rule, Step6Result step6Result) {
		Set<String> ruleTransaction = new HashSet<>(rule.getLeftHandSide());
		ruleTransaction.addAll(rule.getRightHandSide());
		int count = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getItems().containsAll(ruleTransaction)) {
				count++;
			}
		}
		return count;
	}

	private boolean isAddEntitlementRule(AssociationRule rule) {
		for (String val : rule.getRightHandSide()) {
			if (val.contains("ADDED")) {
				return true;
			}
		}
		return false;
	}

}
