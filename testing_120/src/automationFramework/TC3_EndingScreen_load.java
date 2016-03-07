package automationFramework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.text.StrBuilder;
import org.eclipse.jetty.servlets.ConcatServlet;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import sandbox.AudioPlayerExample1;

import org.openqa.selenium.JavascriptExecutor;

public class TC3_EndingScreen_load {

	public static TestResult RunTestCase3(WebDriver driver, String browserName, Integer testId) throws InterruptedException, IOException  {
		// prepare empty log file
		//PrintStream outLogs = new PrintStream(new FileOutputStream(Consts.outputLogFile+"_"+testId+"_"+GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));
		
		// collecting the errors in  each step
		StrBuilder pageErrors = new StrBuilder();
		StrBuilder log = new StrBuilder();
		StrBuilder summary = new StrBuilder(); // for the summary
		
		log.appendln("-------------- TC "+ testId +" "+  driver + " Log output ----------------");
				
		Integer layoutNumber = 120;
		// import text file for Test URLS
		Scanner sc = new Scanner(new File(Consts.inputUrlsTxtFile));
		List<String> lines = new ArrayList<String>();
								
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		String[] URLlist = lines.toArray(new String[0]);
		driver.manage().window().maximize();
				
		System.out.println("===========  URL loop  ===========  " + browserName + "  ===========  TC " + testId + "  ============================");
		// loop to run on each URL 
		
		for (Integer i = 0; i < URLlist.length; i++) {
			main.urlRunId ++;
			summary.append(main.urlRunId);
			
			log.appendln("\nURL run ID: " + main.urlRunId + "\nTest run:: XXX_CHANGE_ME_XXX");
			log.appendln("*******  " + URLlist[i]);
			boolean testRunSuccessfull = true;
			
			
			// check loading time until test script starts
			long secBeforeLoadingURL = System.currentTimeMillis();
			
			// Step 1 - open target url
			
			//implicit timeout for ALL elements
			long maxPageRunTime = (60+25); // 60 for page load + 25 for the test
			driver.manage().timeouts().pageLoadTimeout(maxPageRunTime, TimeUnit.SECONDS);
			try{
				driver.get(URLlist[i]);	
			}catch(Exception e){
				System.out.println("page waited "+maxPageRunTime+" seconds. we cant wait so we continue to next url");
				continue;				
			}
			
			
			System.out.println("------------ " +browserName+ " ---------  TC " + testId + " --------  URL " + (i+1) + " -----------------------------");
			
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
				//scroll down to layer
     			/*  JAVA Style	
			 	WebElement layer = driver.findElement(By.id("InContent-container-centerWrapper0"));
				Actions actions = new Actions(driver);
				actions.moveToElement(layer);
				*/
				
				// check if the layout is 120. if not - end
				String layerType = driver.findElement(By.className(Consts.CENTER_WRAPPER_ID)).getAttribute("layertype");
				Integer layerNumber = Integer.parseInt(layerType);
				if(layerNumber == layoutNumber){
				
					
					// javaScript Style
					boolean wrapper = driver.findElements(By.id(Consts.CENTER_WRAPPER_ID+0)).size() != 0;
					if(wrapper){  // eliminate bug when wrapper didnt load
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Wrapper exists");
						// scroll down to layer to eliminate bug that cant click because of footer ads
						JavascriptExecutor jse = (JavascriptExecutor) driver;
						jse.executeScript("document.getElementById('InContent-container-centerWrapper0').scrollIntoView(true);");
						jse.executeScript("window.scrollBy(0,-150)");
																
						// Step 2- click 10 times until Ending Screen opens
						
						Integer clicksTrue = 0;
						while(clicksTrue < 10){
							
							//click to next item
							driver.findElement(By.id(Consts.TRUE_BTN_ID)).click();
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - click no. " + (clicksTrue + 1) + " occurred");
							Thread.sleep(1800);
							clicksTrue++;
						}									
						System.out.println("finished 10 clicks");
						Thread.sleep(1500);
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - waited 1.5 second");
						// ?? add "continue if" ES wrapper appears ?? //
						
						// step 3- check msg container (the circle)
							// check "next quiz"
						
						boolean MsgContainerTitle = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_TITLE_CLASS)).isDisplayed();
											
						if (MsgContainerTitle) {
							String scoreUnit = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_TITLE_CLASS)).getText();
							System.out.println("YEAH!- message title was displayed: "+scoreUnit);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - message title was displayed: "+scoreUnit);
						} else {
							errors.add("Browser: " + browserName + " URL: " + (i + 1) + " SHAYSE- message title WASNT displayed...");
							System.out.println("SHAYSE- message title WASNT displayed...");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - message title WASNT displayed ");
							testRunSuccessfull = false;
						}
						
						// step 3.1- name of next game
						
						boolean MsgContainerName = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_NAME_CLASS)).isDisplayed();
											
						if (MsgContainerName) {
							String scoreUnit = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_NAME_CLASS)).getText();
							System.out.println("YEAH!- message name was displayed: " + scoreUnit);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - message name was displayed: " + scoreUnit);
						} else {
							errors.add("Browser: " + browserName + " URL: " + (i + 1) + " SHAYSE- message name WASNT displayed...");
							System.out.println("SHAYSE- message name WASNT displayed...");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - message name WASNT displayed");
							testRunSuccessfull = false;
						}
						
						// step 3.2  -- button "next"
						
						boolean MsgContainerBtn = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_BTN_CLASS)).isDisplayed();
											
						if (MsgContainerBtn) {
							String scoreUnit = driver.findElement(By.className(Consts.ENDING_SCREEN_MSG_BTN_CLASS)).getText();
							System.out.println("YEAH!- message button was displayed: " + scoreUnit);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - message Button was displayed: " + scoreUnit);
						} else {
							errors.add("Browser: " + browserName + " URL: " + (i + 1) + " SHAYSE- message button WASNT displayed...");
							System.out.println("SHAYSE- message button WASNT displayed...");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - message button WASNT displayed");
							testRunSuccessfull = false;
						}
						
						// Step 4- check score unit
							// if score title displayed
						
						boolean scoreUnitTitle = driver.findElement(By.className(Consts.ENDING_SCREEN_SCORE_UNIT_CLASS)).isDisplayed();
							
						String unitTitle = null;
						if (scoreUnitTitle) {
						    unitTitle = driver.findElement(By.className(Consts.ENDING_SCREEN_SCORE_UNIT_CLASS)).getText();
							System.out.println("YEAH!- score unit title was displayed: " + unitTitle);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title was displayed: " + unitTitle);
						} else {
							errors.add("Browser: " + browserName + " URL: " + (i + 1) + " SHAYSE- score unit title WASNT displayed...");
							System.out.println("SHAYSE- score unit title WASNT displayed...");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title WASNT displayed");
							testRunSuccessfull = false;
						}
						
						// if score title is correct
											
							// compare text
						
						Integer IscoreNumber = Integer.parseInt(driver.findElement(By.className(Consts.ENDING_SCREEN_SCORE_NUMBER_CLASS)).getText());
						System.out.println(IscoreNumber);
											
						switch(IscoreNumber){
							case 1: 
							case 2:
								if(Consts.SCORE_1_2.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_1_2);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_1_2);
									  testRunSuccessfull = false;
								}break;
													
							case 3:
							case 4:
								if(Consts.SCORE_3_4.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_3_4);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_3_4);
									  testRunSuccessfull = false;
								}break;
							
							case 5:
							case 6:
								if(Consts.SCORE_5_6.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_5_6);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_5_6);
									  testRunSuccessfull = false;
								}break;
							
							case 7:
							case 8:
								if(Consts.SCORE_7_8.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_7_8);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_7_8);
									  testRunSuccessfull = false;
								}break;
														
							case 9:
								if(Consts.SCORE_9.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_9);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_9);
									  testRunSuccessfull = false;
								}break;
							case 10:
								if(Consts.SCORE_10.equals(unitTitle)){
									System.out.println("score unit title is CORRECT");
									log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - score unit title is correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_10);
								}else{System.out.println("score unit title is NOT Correct");
									  errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- score unit title is NOT Correct");
									  log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - score unit title is NOT correct. CABA scored: " + IscoreNumber + " and got this title: " + Consts.SCORE_10);
									  testRunSuccessfull = false;
								}break;
						}
						
					// step 4.2
						
						boolean bShareTitle = driver.findElement(By.className(Consts.ENDING_SCREEN_SHARE_TITLE_CLASS)).isDisplayed();
						String shareTitle = null;
						if(bShareTitle){
							shareTitle = driver.findElement(By.className(Consts.ENDING_SCREEN_SHARE_TITLE_CLASS)).getText(); 
							System.out.println("YEAH, unit share title was displayed: "+shareTitle);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - unit share title was displayed: " + shareTitle);
						}else{
							System.out.println("SHAYSE, unit share title WASNT displayed");
							errors.add("Browser: "+browserName+" URL: "+(i+1)+"SHAYSE, unit share title WASNT displayed");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - unit share title WASNT displayed");
							testRunSuccessfull = false;
						}
					// step 4.2
						
						boolean bShareTitle2 = driver.findElement(By.className(Consts.ENDING_SCREEN_SHARE_TITLE2_CLASS)).isDisplayed();
						String shareTitle2 = null;
						
						if(bShareTitle2){
							shareTitle2 = driver.findElement(By.className(Consts.ENDING_SCREEN_SHARE_TITLE2_CLASS)).getText(); 
							System.out.println("YEAH, unit share title was displayed: "+shareTitle2);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - unit share title 2 was displayed: " + shareTitle2);
						}else{
							System.out.println("SHAYSE, unit share title WASNT displayed");
							errors.add("Browser: "+browserName+" URL: "+(i+1)+"SHAYSE, unit share title WASNT displayed");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - unit share title WASNT displayed");
							testRunSuccessfull = false;
						}
					// step 5- share icons 
						
						boolean bShareIcons = driver.findElement(By.className(Consts.ENDING_SCREEN_SHARE_ICONS_CLASS)).isDisplayed();
						String ShareIcons = null;
						
						if(bShareIcons){
							System.out.println("YEAH, unit share icons were displayed");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - unit share icons were displayed");
						}else{
							System.out.println("SHAYSE, unit share icons WERENT displayed");
							errors.add("Browser: "+browserName+" URL: "+(i+1)+"SHAYSE, unit share icons WERENT displayed");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - unit share icons WERENT displayed");
							testRunSuccessfull = false;
						}
											
						//step 6 - check if img 10 is displayed and save error if not
						
							// check if img 10 class exists 
						boolean isImgExists = driver.findElements(By.className(Consts.FIRST_IMG_CLASS+10)).size() != 0;
						if(isImgExists = true){
							WebElement imgElement = driver.findElement(By.className(Consts.FIRST_IMG_CLASS+10));	
							String imgSrc = imgElement.getAttribute("src");
							boolean Is10thImg = driver.findElement(By.className(Consts.FIRST_IMG_CLASS+10)).isDisplayed();
							if (Is10thImg) {
								System.out.println("YEAH!- we can see image no.10:\n  " + imgSrc);
								log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - image no.10 displayed: " + imgSrc);
							} else {
								errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- img no.10 WASNT displayed");
								System.out.println("SHAYSE- img no.10 WASNT displayed");
								log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - image no.10 WASNT displayed");
								testRunSuccessfull = false;
							}
						}
						
						// step 7 - is item 10 displayed? 
						
						String textItem = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS+10)).getText();
						boolean isText = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS+10)).isDisplayed();
						if (isText) {
							System.out.println("STRANGE....- text of item no.10 is diplayed, and it says: \n  "+textItem);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - Item no.10 displayed before user clicked start");
							// errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- text of item no.10 WASNT displayed");
							testRunSuccessfull = false;
						} else {
							System.out.println("YEAH- text of item no.10 WASNT displayed...YET");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - Item no.10 wasnt displayed yet- its behind the EndingScreen");
						}
						// step 8 - click start
						String startBtn ="cbolaContent-finalGameMessageButton";
						driver.findElement(By.className(startBtn)).click();
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - click 'START' next game");
						System.out.println("YEAH- clicked 'START' and waiting 1.5 seconds...");
						Thread.sleep(1500);
						
						// step 9 - is item 10 displayed
						isText = driver.findElement(By.className(Consts.FIRST_ITEM_CLASS + 10)).isDisplayed();
						if (isText) {
							System.out.println("YEAH- text of item no.10 is diplayed, and it says: \n  " + textItem);
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "V - text of item no.10 is diplayed");
						} else {
							System.out.println("SHAYSE- text of item no.10 WASNT displayed");
							errors.add("\nBrowser: "+browserName+" URL: "+(i+1)+" SHAYSE- text of item no.10 WASNT displayed");
							log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - text of item no.10 WASNT diplayed");
							testRunSuccessfull = false;
						}
						
					}else{
						System.out.println("--NO container wrapper--");
						log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - --NO container wrapper--");
						testRunSuccessfull = false;
					}// end wrapper check
			
				}else {
					System.out.println("SHAYSE-- the layout is NOT 120. the testCase DOESNT fit. moving to next url");
					log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "X - the layout is NOT 120. the testCase DOESNT fit. moving to next url");
				}
				
				
			}else{
				log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "** STOP ** Scrpit Not Found");
				testRunSuccessfull = false;
				System.out.println("SHAYSE --- no script in page");
				errors.add("Browser: "+browserName+" URL: "+(i+1)+"---SHAYSE--- no script in page. please delete URL from URL source file");
			}
			// Test run time
			long timeF = System.currentTimeMillis();
			long timeTotalForURL = timeF - timeS;
			log.appendln(LocalDateTime.now().format(GeneralUtils.formatter) + "FINISHED Testing URL "+(i+1)+". Run time: " + timeTotalForURL / 1000.0 + " Sec");
			System.out.println("FINISHED Testing URL "+(i+1)+". Run time: " + timeTotalForURL / 1000.0 + " Sec");
			
			//System.out.println(DurationFormatUtils.formatDuration(timeTotal, "HH:mm:ss:SS")); // also works
			
			if (!errors.isEmpty()) 
			{
				pageErrors.append(URLlist[i], errors);
			}
			if(testRunSuccessfull){
				log.replaceAll("XXX_CHANGE_ME_XXX", "Successful");
				summary.appendln("\tSuccessful");
			}else{
				log.replaceAll("XXX_CHANGE_ME_XXX", "FAILED");
				summary.appendln("\tFAILED");
			}
				
		
		}

		//print log file 
		//outLogs.print(log);
		
		// Step 9- SUMMARY & close page
		System.out.println("SUMMARY - TC 3 on "+browserName+" browser:\n");
		if(!pageErrors.isEmpty()){
			System.out.println(pageErrors);
		}else {
			System.out.println(" Flawless Victory !!\n");
			AudioPlayerExample1.main(null);
			};
		
				
		driver.close();
				
		//PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\output\\output_"+System.currentTimeMillis()+"txt"));
		//out.println(pageErrors);
		return new TestResult(log, pageErrors, summary);
	}


	
}
