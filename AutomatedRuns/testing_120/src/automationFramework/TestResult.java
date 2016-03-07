package automationFramework;

import org.apache.commons.lang3.text.StrBuilder;

public class TestResult {

		public StrBuilder runLog;
		public StrBuilder runErrors;
		public String testId;
		public String driver;
		
		public TestResult(StrBuilder runLog, StrBuilder runErrors)
		{
			this.runLog = runLog;
			this.runErrors = runErrors;
		}
	
	
}
