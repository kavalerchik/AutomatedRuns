package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasicAdTests {
	
	public static boolean CheckAdAppearance(Integer appearance, WebDriver driver, List<String> errors, String browserName, Integer i){
		boolean isAppearance = false;
		switch(appearance){
		case 1: return CheckAdAppearance1(driver, errors, browserName, i);
		case 2:
			
			break;
		case 3:
			
			break;
		case 4: return CheckAdAppearance4(driver, errors, browserName, i);

		
		}
		return isAppearance;
	}
	
	public static boolean CheckAdAppearance1(WebDriver driver, List<String>errors ,String browserName, Integer i){ 
		WebElement element = BasicTests.GetElement(driver, "cbolaBanner0_1");
		if (element != null && element.isDisplayed()) {
			System.out.println("YEAH! -AD Appearance 1 was loaded succefuly and displayed on screen");
			return true;
		} else {
			java.util.Date dateNow = new java.util.Date();
			errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- AD App1 WASNT Displayed on sec "+dateNow+", but its in the page");
			System.out.println("SHAYSE- AD Appearance 1 WASNT displayed on sec "+dateNow+", but its in the page");
			return false;
		}		
	}
	
	public static boolean CheckAdAppearance4(WebDriver driver, List<String> errors, String browserName, Integer i){
		WebElement element = BasicTests.GetElement(driver, "cbolaBanner0_4");		
		if (element != null && element.isDisplayed()) {
			System.out.println("YEAH! -AD Appearance 4 was loaded succefuly and displayed on screen");
			return true;
		} else {
			java.util.Date dateNow = new java.util.Date();
			errors.add("Browser: "+browserName+" URL: "+(i+1)+" SHAYSE- lower AD WASNT LOADED at: "+dateNow);
			System.out.println("SHAYSE- AD Appearance 4 WASNT LOADED at "+dateNow);
			return false;
		}
	}
}
