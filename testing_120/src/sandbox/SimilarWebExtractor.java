package sandbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.lang3.text.StrBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import automationFramework.GeneralUtils;

public class SimilarWebExtractor {

	public static long totalTestingTime = 0;	
	public static long totalPagesLoadTime = 0;
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
						
				long timeTCStart = System.currentTimeMillis();
									
				// setting Chrome Driver (the driver opens a Server application)
				String exePathChromeDriver = "C:\\Users\\Yair\\Documents\\yair\\QA\\TestAutomation\\Selenium\\chrome_driver2_0\\chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", exePathChromeDriver);
				WebDriver driverChrome = new ChromeDriver();
								
						
				// create log file object  		
				PrintStream outLogs = new PrintStream(new FileOutputStream("C:\\Users\\Yair\\Documents\\yair\\QA\\similarWebExtractor\\output_similarWeb_"+GeneralUtils.sdf.format(GeneralUtils.date)+".txt"));
				
				StrBuilder log = new StrBuilder();
								
				// import text file for Test URLS
				Scanner sc = new Scanner(new File("C:\\Users\\Yair\\Documents\\yair\\QA\\similarWebExtractor\\input_domains.txt")); //input_domains_166.txt
				List<String> lines = new ArrayList<String>();
									
				while (sc.hasNextLine()) {
					lines.add(sc.nextLine());
				}
				
				String[] URLlist = lines.toArray(new String[0]);
				for (Integer i = 0; i < URLlist.length; i++) {
					System.out.println("-----  URL " + (i+1) + " --------- "+ URLlist[i]+" --------------------");
					//long timeToWait = 2;
					//driverChrome.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.MINUTES);
					driverChrome.get("https://www.similarweb.com/website/www."+URLlist[i]);
					long randomNumber = 5 + (int)(Math.random() * 5);
					System.out.println(randomNumber * 2000);
					Thread.sleep((long) randomNumber * 2000);
					
					String coughtMe = "Sorry, you have been blocked";
					if(driverChrome.findElement(By.xpath("/html/body/section/div/div[2]/h1")).equals(coughtMe)){
						driverChrome.close();
					}else{
						
						try{
							
							WebElement totalVisits = driverChrome.findElement(By.xpath("/html/body/div[3]/div/div/div[4]/div[2]/div[2]/div/div[2]/div/span[2]"));
							String stringTotalVisits = totalVisits.getText();
							stringTotalVisits = stringTotalVisits.substring(0, stringTotalVisits.length()-1);
							String stringTotalVisitsSymbol = totalVisits.getText().substring(stringTotalVisits.length());
							double doubleTotalVisits = Double.parseDouble(stringTotalVisits);
							System.out.println(doubleTotalVisits);
							System.out.println(stringTotalVisitsSymbol);
							
							WebElement pageViews = driverChrome.findElement(By.xpath("/html/body/div[3]/div/div/div[4]/div[2]/div[2]/div/div[4]/div/span[2]"));
							String stringPageViews = pageViews.getText();
							double doublePageViews = Double.parseDouble(stringPageViews);
							System.out.println(doublePageViews);
							
							Integer result = 0;
							if(stringTotalVisitsSymbol.equals("K") && stringTotalVisitsSymbol != null){
								result = (int) (doubleTotalVisits * doublePageViews * 1000);
								System.out.println(result);
							}else if(stringTotalVisitsSymbol.equals("M") && stringTotalVisitsSymbol != null){
								result = (int)(doubleTotalVisits * doublePageViews * 1000000);
								System.out.println(result);
							}else {System.out.println("result not correct");}
													
							log.append(URLlist[i] + "\t" + result + "\n");	
							outLogs.flush();
							outLogs.println(log); // just in case it brakes, to save the data all the time
							
						}catch(Exception e){
							
						}continue;

											
					}
						
											
				}
				outLogs.println(log);
				
	}

}
