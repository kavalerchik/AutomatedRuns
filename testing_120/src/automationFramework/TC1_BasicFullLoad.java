package automationFramework;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // import file (urls4tests)
import java.util.Scanner; // import file (urls4tests)
import java.util.concurrent.TimeUnit;
import java.io.File; // import file (urls4tests)
import java.io.FileOutputStream;

import org.apache.commons.lang3.text.StrBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
//import com.sun.jna.platform.FileUtils;


// method - catch exceptions
public class TC1_BasicFullLoad {
	
	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */

// Method "Run test case 1"
	public static TestResult RunTestCase1(WebDriver driver, String browserName, Integer testId) throws InterruptedException, IOException  {
				
		// prepare empty log file (no need, there is one in the main)
		//PrintStream outLogs = new PrintStream(new FileOutputStream(Consts.outputLogFile+"_"+testId+"_"+GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));
		
		// declare String Builders 
		StrBuilder pageErrors = new StrBuilder(); // for collecting the errors in  each step (was "Hash Map" before)
		StrBuilder log = new StrBuilder(); // for full logs
		StrBuilder summary = new StrBuilder(); // for the summary
		
		
		
		log.appendln("-------------- TC "+ testId +" "+  browserName + " Log output ----------------");
		
		Integer layoutNumber = 120;
		// import text file for Test URLS
		Scanner sc = new Scanner(new File(Consts.inputUrlsTxtFile));
		List<String> lines = new ArrayList<String>();
							
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		String[] URLlist = lines.toArray(new String[0]);
		driver.manage().window().maximize();
			
		System.out.println("===========  URL loop  ===========  " + browserName + "  ===========  TC " + testId + "  ============================\n\n");
		// loop to run on each URL 
		
		for (Integer i = 0; i < URLlist.length; i++) {
			
			main.urlRunId ++;
			summary.append(main.urlRunId);
			
			log.appendln("\nURL run ID: " + main.urlRunId + "\nTest run:: XXX_CHANGE_ME_XXX");
			log.appendln("*******  " + URLlist[i]);
			boolean testRunSuccessfull = true;
			
			// check loading time until test script starts			
			long secBeforeLoadingURL = System.currentTimeMillis();
			
			System.out.println("------------ " +browserName+ " ---------  TC " + testId + " --------  URL " + (i+1) + " -----------------------------");
			System.out.println(URLlist[i]);
			
			// Step 1 - open target url
			
				//implicit timeout for ALL elements
			long maxPageRunTime = (60+10); // 60 for page load + 10 for the test
			driver.manage().timeouts().pageLoadTimeout(maxPageRunTime, TimeUnit.SECONDS);
			try{
				driver.get(URLlist[i]);	
			}catch(Exception e){
				System.out.println("page waited "+maxPageRunTime+" seconds. SHOW MUST GO ON! continuing to next url");
				log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - loading time exceeded limit. SHOW MUST GO ON!");
				log.replaceAll("XXX_CHANGE_ME_XXX", "FAILED");
				summary.appendln("\tFAILED");
				continue;				
			}
									
		
			// measure time
			long timeS = System.currentTimeMillis();
			
			//print page load duration
			long timePageLoad = timeS - secBeforeLoadingURL;
			log.appendln("URL no."+(i+1)+" loading duration was: "+timePageLoad/1000.0);
			System.out.println("URL no."+(i+1)+" loading duration was: "+timePageLoad/1000.0);
			main.totalPagesLoadTime += timePageLoad; 
			
			List<String> errors = new ArrayList<String>();
			
			System.out.println("** Starting to test URL no." + (i+1) + " on "+browserName+" broswer **");
			Integer PageSource = driver.getPageSource().length();
			
			// Step 2 - check if Carambola SCRIPT was in the HTML. if YES-continue steps if NO-skip steps
			boolean isScript = BasicTests.IsScriptValid(driver, errors, browserName, i);
			if(isScript){
				log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Script Found");
				
				Thread.sleep(1500);
				// check if the layout is 120. if not - end
				String layerTypeAttribute = driver.findElement(By.className(Consts.CENTER_WRAPPER_ID)).getAttribute("layertype");
				Integer layerNumber = Integer.parseInt(layerTypeAttribute);
				System.out.println(layerTypeAttribute);
				
				if(layerNumber == layoutNumber){
					
					System.out.println("YEAH - layout is 120. moving on...");
					// Step 3 Verify Ad appearance 1
					if(BasicAdTests.CheckAdAppearance(1, driver, errors, browserName, i))
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Ad Appearance 1 found");
					else
					{
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Ad Appearance 1 NOT found");	
						File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_Ad Appearance 1 NOT found.png"));
						testRunSuccessfull = false;					
					}
							
		
					System.out.println("Page source length: " + PageSource);
					// Step 3.1 wait 5 sec
					//Thread.sleep(5000);
					
					// Step 3.2 Verify Ad appearance 1 after 5 sec. (making sure the ad is closed, but still alive)
					BasicAdTests.CheckAdAppearance(1, driver, errors, browserName, i);
					log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Ad Appearance 1 found");
					
					// Step 4 - check if carambola centerWrapper got opened
					//boolean CbolaScriptStatus = driver.findElements(By.id("InContent-container-centerWrapper0")).size() != 0;
					boolean centerWrapperExists =  BasicTests.CheckCenterWrapper(layoutNumber, driver, errors, browserName, i);
					if(centerWrapperExists){	
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - center wrapper exists");
						// Step 4.1 - check if cbola board was displayed (the actual game)
														
						WebElement CbolaBoardStatus = driver.findElement(By.className(Consts.BOARD_CLASS));
						if (CbolaBoardStatus != null && CbolaBoardStatus.isDisplayed()) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - cbola board displayed");
							System.out.println("YEAH!- cbola board was displayed");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - cbola board WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- cbola borad is NOT displayed");
							System.out.println("SHAYSE- cbola borad is NOT displayed"); // need to check on a case where half the board is displayed. do we catch it??
							testRunSuccessfull = false;
							if(CbolaBoardStatus == null){
								log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - cbola board WASNT loaded");
								System.out.println("SHAYSE- cbola board wasnt loaded");
								testRunSuccessfull = false;  // NEED TO CONSIDER if it fails..
								
							}
						}
						// Step 4.2 - Verify 1st Image -cbolaContent-imageLoader
						
						boolean CbolaFirstImg = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS+0)).isDisplayed();
						if (CbolaFirstImg) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Image 1 was displayed");
							System.out.println("YEAH!- we can see 1st image element:");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Image 1 WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- 1st img element WASNT displayed");
							System.out.println("SHAYSE- 1st img element WASNT displayed");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_1st img element WASNT displayed.png"));
							testRunSuccessfull = false;
						}
						WebElement ImageFile = driver.findElement(By.className(Consts.FIRST_IMG_CLASS+0));
			
						// get the src of the img
						WebElement img0 = driver.findElement(By.className(Consts.FIRST_IMG_CLASS+0));
						String src0 = img0.getAttribute("src");
						System.out.println(src0);
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Image 1 src is: " + src0);
						
						// Step 4.3 - verify 1st item's text
						boolean isText0 = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS+0)).isDisplayed();
						
						if (isText0) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Item 1 was displayed");
							System.out.println("YEAH!- 1st text is diplayed, and it says: ");
							// WebElement Text0e =
							// driver.findElement(By.className(Text0class));
							// String Text0s = Text0e.getAttribute("td");
							String Text0s = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS+0)).getText();
							System.out.println(Text0s);
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Item 1 WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- 1st text WASNT displayed");
							System.out.println("SHAYSE- 1st text WASNT displayed");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_1st text WASNT displayed.png"));
							testRunSuccessfull = false;
						}
						// Step 4.4- verify SHARE btn FB
						
						boolean ShareFBb = driver.findElement(By.className(Consts.SHARE_FB_CLASS)).isDisplayed();
						if (ShareFBb) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - FB share btn was displayed");
							System.out.println("YEAH!- FB btn was displayed");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - FB share btn WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+"SHAYSE- FB btn not displayed");
							System.out.println("SHAYSE- FB btn not displayed");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_FB btn WASNT displayed.png"));
							testRunSuccessfull = false;
						}
			
						// Step 4.5- verify SHARE btn Twitter
						
						boolean ShareTwitterb = driver.findElement(By.className(Consts.SHARE_TWITTER_CLASS)).isDisplayed();
						if (ShareTwitterb) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Twitter share btn was displayed");
							System.out.println("YEAH!- Twitter btn was displayed");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Twitter share btn WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+"SHAYSE- Twitter btn not displayed");
							System.out.println("SHAYSE- Twitter btn NOT displayed");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_Twitter btn WASNT displayed.png"));
							testRunSuccessfull = false;
						}
			
						// Step 5- check TEXTs
							// score title
						String ScoreTitle = driver.findElement(By.className(Consts.SCORE_TITLE_CLASS)).getText();
						System.out.println("ref text is: " + Consts.TEXT_QUESTION);
						System.out.println("text on screen is: " + ScoreTitle);
						if (new String(Consts.TEXT_QUESTION).equals(ScoreTitle)) { // ScoreTitle == text_Question . why doesn't it works?...
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Text check- score title match");
							System.out.println("the score_title matches the ref text");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Text check- score title DOSENT match");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+"SHAYSER-there is NO match between the score title texts");
							System.out.println("SHAYSE - there is NO match between the score title texts");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_NO match between the score title texts.png"));
							testRunSuccessfull = false;
						}
			
						// Step 5.2 - score unit
						WebElement ScoreUnitElement = driver.findElement(By.className(Consts.SCORE_UNIT_CLASS));
						String ScoreUnit = ScoreUnitElement.findElement(By.tagName("span")).getText();
						System.out.println("ref text is: " + Consts.TEXT_YOUR_SCORE);
						System.out.println("text on screen is: " + ScoreUnit);
			
						// score unit
						if (new String(Consts.TEXT_YOUR_SCORE).equals(ScoreUnit)) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Text check- score unit match");
							System.out.println("the score_units matches the ref text");
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Text check- score unit DOESNT match");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+"SHAYSE-there is NO match between the unit_score texts");
							System.out.println("SHAYSER-there is NO match between the unit_score texts");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_NO match between the unit_score texts.png"));
							testRunSuccessfull = false;
						}
						
						// Step 5.4- verify items Title
						
						boolean Titleb = driver.findElement(By.className(Consts.TITLE_CLASS)).isDisplayed();
						if (Titleb) {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Text check- Title was displayed");
							System.out.println("YEAH!- Title was displayed: ");
							String Titles = driver.findElement(By.className(Consts.TITLE_CLASS)).getText();
							System.out.println(Titles);
						} else {
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Text check- Title WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+"SHAYSE - Title WASNT displayed");
							System.out.println("SHAYSE- Title WASNT displayed");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_Title WASNT displayed.png"));
							testRunSuccessfull = false;
						}
			
						// Step 6 - wait 5 sec
						Thread.sleep(5000);
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - wait 5 sec");
						
						// Step 7 - check if AD was loaded in Appearance 1
						if(BasicAdTests.CheckAdAppearance(1, driver, errors, browserName, i)){
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Ad Appearance 1 found");
						}else{
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X  Ad Appearance 1 NOT found");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_Ad Appearance 1 NOT found.png"));
							testRunSuccessfull = false;
						}
						
						// Step 8 - check if Lower AD was loaded in Appearance 4
						if(BasicAdTests.CheckAdAppearance(4, driver, errors, browserName, i)){
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Ad Appearance 4 found");
						}else{
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Ad Appearance 4 NOT found");
							File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
							FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_Ad Appearance 4 NOT found.png"));
							testRunSuccessfull = false;
						}
							
					 }else{
						 System.out.println("SHAYSE-- the script is in the page, BUT the WRAPPER didnt LOADED");
						 errors.add("Browser: "+browserName+" URL: "+(i+1)+"SHAYSE-- the script is in the page, BUT the WRAPPER didnt LOADED");
						 log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "** STOP ** Center Wrapper DOES NOT exist");
						 testRunSuccessfull=false;
					 }
					}else {
						System.out.println("SHAYSE-- the layout is NOT 120. the testCase DOESNT fit. moving to next url");
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - the layout is NOT 120. the testCase DOESNT fit. moving to next url");
						File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(scrFile, new File(Consts.outputDirectory + "screenshot_"+ browserName +"_url_"+ i +"_the layout is NOT 120.png"));
					}
				
				}else{
					log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "** STOP ** Script Not Found");
					testRunSuccessfull=false;
					System.out.println("SHAYSE --- no script in page");
					errors.add("Browser: "+browserName+" URL: "+(i+1)+"---SHAYSE--- no script in page. please delete URL from URL source file");
				}
	
			// Test run time
			long timeF = System.currentTimeMillis();
			long timeTotalForURL = timeF - timeS;
			//System.out.println(timeS);
			//System.out.println(timeF);
			log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "FINISHED Testing URL "+(i+1)+". Run time: " + timeTotalForURL / 1000.0 + " Sec");
			System.out.println("FINISHED Testing URL "+(i+1)+". Run time: " + timeTotalForURL / 1000.0 + " Sec");
			//System.out.println(DurationFormatUtils.formatDuration(timeTotal, "HH:mm:ss:SS")); // also works
			
			
			
			if (!errors.isEmpty()) 
			{
				pageErrors.append(URLlist[i], errors) ; //put(URLlist[i], errors);
			}
			
			if(testRunSuccessfull){
				log.replaceAll("XXX_CHANGE_ME_XXX", "Successful");
				summary.appendln("\tSuccessful");
				}
			else{
				log.replaceAll("XXX_CHANGE_ME_XXX", "FAILED");
				summary.appendln("\tFAILED");
			}

			
		}				
		//print to log TXT 
		//outLogs.println(log);  // bug? doesnt print all the browsers. only the last one...probably override the one before it
		
		
		// Step 9- SUMMARY & close page
		System.out.println("\nSUMMARY - test case 1 on "+browserName+" browser:");
		System.out.println(pageErrors.toString());
				
		driver.close();
		
			
		//PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\output\\output_"+System.currentTimeMillis()+"txt"));
		//out.println(pageErrors);
		//return pageErrors;
		
		return new TestResult(log, pageErrors, summary);
	}

}
