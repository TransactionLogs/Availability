package org.anonymous.transactionlogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.anonymous.transactionlogs.model.AssociationRuleWrapper;
import org.anonymous.transactionlogs.model.Itemset;
import org.anonymous.transactionlogs.steps.ruleanalysis.AssociationRuleAnalyzer;
import org.anonymous.transactionlogs.steps.step1.Step1FromCSV;
import org.anonymous.transactionlogs.steps.step1.Step1Input;
import org.anonymous.transactionlogs.steps.step1.Step1Result;
import org.anonymous.transactionlogs.steps.step2.Step2;
import org.anonymous.transactionlogs.steps.step2.Step2Input;
import org.anonymous.transactionlogs.steps.step2.Step2Result;
import org.anonymous.transactionlogs.steps.step3.Step3;
import org.anonymous.transactionlogs.steps.step3.Step3Input;
import org.anonymous.transactionlogs.steps.step3.Step3Result;
import org.anonymous.transactionlogs.steps.step4.Step4;
import org.anonymous.transactionlogs.steps.step4.Step4Input;
import org.anonymous.transactionlogs.steps.step4.Step4Result;
import org.anonymous.transactionlogs.steps.step5.Step5;
import org.anonymous.transactionlogs.steps.step5.Step5Input;
import org.anonymous.transactionlogs.steps.step5.Step5Result;
import org.anonymous.transactionlogs.steps.step6.Step6;
import org.anonymous.transactionlogs.steps.step6.Step6Input;
import org.anonymous.transactionlogs.steps.step6.Step6Result;
import org.anonymous.transactionlogs.steps.step7.Step7;
import org.anonymous.transactionlogs.steps.step7.Step7Input;
import org.anonymous.transactionlogs.steps.step7.Step7Result;

public class TransactionLogsMain {

	public static void main(String[] args) {
		new TransactionLogsMain().run();
	}

	public TransactionLogsMain() {

	}

	public void run() {

		// DEFINE PARAMETERS
		File logInputFile = new File("src/main/resources/sample_events.csv");
		//				File logInputFile = new File("src/main/resources/sample_events_tiny.csv");

		Set<String> relevantAttributes = Set.of("orgunit", "employeeType", "function", "location");
		// leave null to analyze all entitlements:
		Set<String> relevantEntitlements = null;
		//				Set<String> relevantEntitlements = Set.of("'Consultant' (b90e08b8-0bbf-4cff-8613-fa67ccaa3466)");

		long slidingWindowLength = 1000 * 60 * 60 * 24; // one day in milliseconds
		double minimumSupport = 0.6;
		double minimumConfidence = 0.6;

		// START EXECUTION
		// STEP 1: Fetch & parse logs. It must be implemented for a given data source. While we worked on relational database during the analysis of industrial IAM data, we provide a simple CSV file reader without external dependencies here.
		System.out.println("Executing Step 1: Fetch & parse logs.");
		Step1Input step1Input = new Step1Input(logInputFile);
		Step1Result step1result = new Step1FromCSV().execute(step1Input);
		System.out.println("");

		// STEP 2: Limit to relevant attributes
		System.out.println("Executing Step 2: Limit to relevant attributes.");
		Step2Input step2Input = new Step2Input(step1result, relevantAttributes);
		Step2Result step2result = new Step2().execute(step2Input);
		System.out.println("");

		// STEP 3: Limit to relevant entitlements
		System.out.println("Executing Step 3: Limit to relevant entitlements.");
		Step3Input step3Input = new Step3Input(step2result, relevantEntitlements);
		Step3Result step3Result = new Step3().execute(step3Input);

		// From here on we split the  processing into sub-processes. Each sub-process analyzed only one entitlement. This means that every set of association rules will also be executed for exactly one entitlement.
		System.out.println("Executing steps 4 to 7 parallel for each entitlement.");

		System.out.println("Rules: [frequency][confidence] (condition) -> (implication)");

		step3Result.getIncludedEntitlements().parallelStream().forEach(analyzedEntitlement -> {
			Step3Result step3ResultLocal = step3Result.cloneAndFilter(analyzedEntitlement);

			// STEP 4: Partition by digital identity
			Step4Input step4Input = new Step4Input(step3ResultLocal);
			Step4Result step4Result = new Step4().execute(step4Input);

			// STEP 5: Apply sliding window to create item sets
			Step5Input step5Input = new Step5Input(analyzedEntitlement, step4Result, slidingWindowLength);
			Step5Result step5Result = new Step5().execute(step5Input);

			// Individual item sets were created by analyzing the log partitions for each digital identity. Now we can add them into a global collection again to mine frequent item sets.
			List<Itemset> allItemsets = new ArrayList<>();
			for (String partition : step5Result.getPartitions()) {
				List<Itemset> itemsetsForPartition = step5Result.getItemsets(partition);
				allItemsets.addAll(itemsetsForPartition);
			}

			// Step 6: Mine frequent item sets
			Step6Input step6Input = new Step6Input(allItemsets, minimumSupport);
			Step6Result step6Result = new Step6().execute(step6Input);

			// Step 7: Generate association rules
			Step7Input step7Input = new Step7Input(analyzedEntitlement, step6Result, minimumConfidence);
			Step7Result step7Result = new Step7().execute(step7Input);

			// All done. Now The generated rules can be analyzed.
			analyzeResults(analyzedEntitlement, step7Result);

		});

	}

	private synchronized void analyzeResults(String analyzedEntitlement, Step7Result result) {

		// see result.getAnalysisResult() for the original frequent itemsets and association rules as generated by the Apriori library.

		// These are ALL rules that Apriori could find for the given item sets.
		List<AssociationRuleWrapper> rules = result.getWrappedAssociationRules();

		AssociationRuleAnalyzer analyzer = new AssociationRuleAnalyzer();

		// This is the filtering we applied before screening rules manually
		List<AssociationRuleWrapper> rulesInScope = analyzer.filterRulesInScope(rules);
		if (rulesInScope.isEmpty()) {
			return;
		}
		// Useful for screening: Sort rules with more observed item sets and higher frequency to the top
		analyzer.sort(rules);

		System.out.println("");

		if (rules.isEmpty()) {
			// nothing to analyze
			System.out.println("No association rules generated for entitlement " + analyzedEntitlement + ".");
			return;
		}

		System.out.println(
				"Found " + rulesInScope.size() + " rules in scope for manual screening (" + rules.size() + " total):");
		for (AssociationRuleWrapper rule : rulesInScope) {
			System.out.println(rule.toString());
		}

	}

}
