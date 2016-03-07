package automationFramework;

import org.apache.commons.lang3.text.StrBuilder;

public class TestResult {

		public StrBuilder runLog;
		public StrBuilder runErrors;
		public StrBuilder runSummary;
		public String testId;
		public String driver;
		
		public TestResult(StrBuilder runLog, StrBuilder runErrors, StrBuilder runSummary)
		{
			this.runLog = runLog;
			this.runErrors = runErrors;
			this.runSummary = runSummary;
		}
	
	
}
