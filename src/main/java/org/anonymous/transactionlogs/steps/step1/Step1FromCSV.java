package org.anonymous.transactionlogs.steps.step1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.anonymous.transactionlogs.model.LogEntryType;
import org.anonymous.transactionlogs.model.NormalizedLogEntry;
import org.anonymous.transactionlogs.steps.IDataProcessingStep;

/**
 * Step 1 fetches the logs. It must be implemented for a given data source. While we worked on relational database during the analysis of industrial IAM data, we provide a simple CSV file reader without external dependencies here.
 */
public class Step1FromCSV implements IDataProcessingStep<Step1Input, Step1Result> {

	private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	@Override
	public Step1Result execute(Step1Input step1Input) {

		File inputFile = step1Input.getInputFile();
		List<NormalizedLogEntry> logEntries = retrieveLogEntries(inputFile);
		System.out.println("Loaded " + logEntries.size() + " log entries from CSV.");
		// sort by timestamp
		logEntries.sort(NormalizedLogEntry::compareTo);
		LogEntryProviderImpl logEntryProvider = new LogEntryProviderImpl(logEntries);
		Step1Result result = new Step1Result();
		result.setLogEntryProvider(logEntryProvider);

		return result;

	}

	private List<NormalizedLogEntry> retrieveLogEntries(File csvFile) {

		if (!csvFile.exists()) {
			throw new IllegalArgumentException("Log entry file does not exist: " + csvFile.getAbsolutePath());
		}

		// read line by line
		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

			// first line contains the header ->  skip it
			reader.readNext();

			List<NormalizedLogEntry> ret = new ArrayList<>();

			int limit = 0;

			String[] line;
			while ((line = reader.readNext()) != null) {
				NormalizedLogEntry logEntry = parseCsvLine(line);
				ret.add(logEntry);
			}

			return ret;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvValidationException e) {
			throw new RuntimeException(e);
		}

		return null;

	}

	private NormalizedLogEntry parseCsvLine(String[] line) {

		// read raw values
		String timestampStr = line[0];
		String typeStr = line[1];
		String identityStr = line[2];
		String valueStr = line[3];
		// separated for easier processing
		String attributeStr = nullable(line[4]);
		String entitlementStr = nullable(line[5]);

		// convert them to required data types
		LocalDateTime timestamp = parseTimestamp(timestampStr);
		LogEntryType type = LogEntryType.valueOf(typeStr);

		NormalizedLogEntry logEntry = new NormalizedLogEntry(type, timestamp, identityStr, valueStr).withAttributeName(
				attributeStr).withEntitlementName(entitlementStr);

		return logEntry;

	}

	private String nullable(String str) {
		return str == null || str.isEmpty() || str.equalsIgnoreCase("NULL") ? null : str;
	}

	private LocalDateTime parseTimestamp(String timestampStr) {

		// a bit hacky but not a problem
		int dotIndex = timestampStr.lastIndexOf('.');
		if (dotIndex == -1) {
			timestampStr += ".000";
		} else {
			String milliseconds = timestampStr.substring(dotIndex + 1);
			while (milliseconds.length() < 3) {
				milliseconds += "0";
			}
			timestampStr = timestampStr.substring(0, dotIndex + 1) + milliseconds;
		}

		return LocalDateTime.parse(timestampStr, timestampFormatter);

	}

}
