package automationFramework;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.james.mime4j.field.datetime.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class main {

	public static long totalTestingTime = 0;	
	public static long totalPagesLoadTime = 0;
	// public static Integer totalURLs = null;  TO DO
			    		
	// MAIN 
		public static void main(String[] args) throws InterruptedException, IOException {
			//delete me
			RunTestForAllBrowsers(1);
			RunTestForAllBrowsers(2);
			RunTestForAllBrowsers(3);
			
			System.out.println("\n\n $$$$$$$$$$ there must be a god cause all TCs finished without exploding  $$$$$$$$$$$$\n"
					+ "Summary:");
			System.out.println("Testing time for ALL TCs was: " + (totalTestingTime)/1000.0/60 +" minutes");
			System.out.println("Time for loading ALL pages in ALL TCs was: " + (totalPagesLoadTime)/1000.0/60 +" minutes");
			System.out.println("Net Testing time for ALL pages in ALL TCs was: " + (totalTestingTime - totalPagesLoadTime)/1000.0/60 +" minutes");
			
		}
					
		/**
		 * @param testId
		 * @throws InterruptedException
		 * @throws IOException
		 */
		private static void RunTestForAllBrowsers(int testId) throws InterruptedException, IOException {
			
			long timeTCStart = System.currentTimeMillis();
			
			Vector<TestResult> testResults = new Vector<TestResult>();	// Similar to ArrayList. very useful if you don't know the size of the array in advance or you just need one that can change sizes over the lifetime of a program.		
			
			// *************   setting Internet Explorer driver  **********************
			/*
			String exeServiceIEdriver = "C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\IEdriver2_48\\IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", exeServiceIEdriver);
			InternetExplorerDriver driverIE = new InternetExplorerDriver();
			HashMap<String, List<String>> resIE = RunTest(testId, driverIE, "IE"); 
			*/
			
			// ***************  Setting FireFox driver **********************
			WebDriver driverFF = new FirefoxDriver();
			//HashMap<String, List<String>> resFF = RunTest(testId, driverFF, "Firefox"); 
			testResults.addElement(RunTest(testId, driverFF, "Firefox"));
			
							
			// ****************  setting Chrome Driver (the driver opens a Server application)   **********************
			String exePathChromeDriver = "C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\chrome_driver2_0\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", exePathChromeDriver);
			WebDriver driverChrome = new ChromeDriver();			
			testResults.addElement(RunTest(testId, driverChrome, "Chrome"));
			
			//Print to Files
			
			// export TXT file for Errors		
			WriteToFile(testResults, "C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\output\\output_TC_"+testId+"_");
			
			PrintStream outErrs = new PrintStream(new FileOutputStream("C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\output\\output_Errors_TC_"+testId+"_"+GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));
			
			// export TXT file for Logs ==> located in each TC		
			//PrintStream outLogs = new PrintStream(new FileOutputStream("C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\output\\output_Logs_TC_"+testId+"_"+GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));
			
			//outErrs.println(resIE);
			/*
			outErrs.println("\n** FireFox results **\n");
			outErrs.println(resFF.runErrors);
			outErrs.println("\n** Chrome results **\n");
			outErrs.println(resChrome);
			*/
			
			long timeTCFinish = System.currentTimeMillis();
			long timeTCTotal = timeTCFinish - timeTCStart;
			System.out.println("\n## Test case "+ testId +" Finished ## \n"
					+ LocalDateTime.now() + "\n\n" 
					+ "Total time for TC "+testId+" on all browsers was:"+timeTCTotal / 1000.d / 60 +" minutes ##");
			//java.util.Date dateEndTest = new java.util.Date();
			//System.out.println(dateEndTest);
						
			if(outErrs != null){
				System.out.println("\ntxt file was created succesfully");
			}
			
			// not accurate
			System.out.println("Total time for loading all pages in TC "+testId+" was: "+totalPagesLoadTime/1000.0/60+" minutes");
			System.out.println("Net testing time in TC "+testId+" was: " + (timeTCTotal - totalPagesLoadTime)/1000.0/60 +" minutes");
			
			totalTestingTime += timeTCTotal;
	
		}
		
		// choose the test case according to the number given in MAIN
		private static TestResult RunTest(int testId, WebDriver driver, String browserName) throws InterruptedException, IOException {
			TestResult res = null;
			switch (testId) {
				case 1:
					res = TC1_BasicFullLoad.RunTestCase1(driver, browserName, testId);				
				break;
				case 2:
					res = TC2_HalfGame.RunTestCase2(driver, browserName, testId);
					break;
				case 3:
					res = TC3_EndingScreen_load.RunTestCase3(driver, browserName, testId);
					break;
				default: 
					break;
				}
			return res;
		}
		
		private static void WriteToFile(Vector<TestResult> outputs, String prePath) throws FileNotFoundException{
			
			PrintStream myFile = new PrintStream(new FileOutputStream(prePath + "Log_" + GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));			
						
			for(TestResult result : outputs){
				myFile.println("*****" + result.driver + "****");
				myFile.println(result.runLog);
			}
			
			myFile = new PrintStream(new FileOutputStream(prePath + "Error_" + GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));			
			for(TestResult result : outputs){
				myFile.println("*****" + result.driver + "****");
				myFile.println(result.runErrors);
			}			
			
		}		
}
