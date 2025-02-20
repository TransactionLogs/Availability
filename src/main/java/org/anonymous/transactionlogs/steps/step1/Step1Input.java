package org.anonymous.transactionlogs.steps.step1;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class Step1Input {

	private final File inputFile;

	public Step1Input(File inputFile) {
		this.inputFile = Objects.requireNonNull(inputFile);
	}

	public File getInputFile() {
		return inputFile;
	}
}
