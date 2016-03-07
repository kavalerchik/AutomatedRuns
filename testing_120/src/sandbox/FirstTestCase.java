package sandbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirstTestCase {

	public static void main(String[] args) throws InterruptedException {
		// Auto-generated method stub

		// Create a new instance of the Firefox driver
				WebDriver driver = new FirefoxDriver();
				
		        //Launch the Online Store Website
				String URL = "http://www.store.demoqa.com"; 
				driver.get(URL);
				
				// define arguments and get data from test page
				String PageTitle = driver.getTitle();
				Integer PageTitleLength = driver.getTitle().length();
				String PageSource = driver.getPageSource();
				Integer PageSourceLength = driver.getPageSource().length(); 
				String PageURL = driver.getCurrentUrl();
				
		        // Print a Log In message to the screen
		        System.out.println("the page's title is: "+PageTitle+" ant it's length is: "+PageTitleLength);
				
				// check IF URL is correct
		        if(PageURL.equals(URL)){
		        	System.out.println("Verification succesful- the specified URL is indeed open");
		        }else{
		        	System.out.println("OOPS, for some reason the URL "+URL+" wasn't opened");
		        	System.out.println("Instead, this is the URL that was opened: "+PageURL);
		        }
				//Wait for 10 Sec
				//Thread.sleep(10000);
				
		        System.out.println("The length of the source page is "+PageSourceLength);
		        // Close the driver
		        //driver.quit();
		        
		        //closing the browser
		        driver.close();
		
		
	}
	
}
