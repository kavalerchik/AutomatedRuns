package sandbox;
	
import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.firefox.FirefoxDriver;


public class NavigateCommands {
		
	public static void main(String[] args) {

		WebDriver driverff = new FirefoxDriver();
		
		String URL = "http://www.DemoQA.com";
		driverff.get(URL);

		driverff.findElement(By.xpath("/html/body/div/div/div[2]/aside[1]/div[2]/div/ul/li/a")).click();
		
		driverff.navigate().back();
		
		driverff.navigate().forward();
		
		driverff.navigate().to(URL);
		
		driverff.navigate().refresh();
		
		driverff.close();
		
	
			
	}

}
