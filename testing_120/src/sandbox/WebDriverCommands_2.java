package sandbox;

	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverCommands_2 {
	public static void main(String[] args){
		WebDriver driver = new FirefoxDriver();
		
		driver.get("http://demoqa.com/frames-and-windows/");
		//driver.findElement(By.xpath(".//*[@id="+"menu-item-140"+"]/a")).click();  // xpath from Chrome - didn't worked !!
		driver.findElement(By.xpath("/html/body/div/div/div[2]/aside[2]/div[2]/div/ul/li[1]/a")).click(); // xpath from FF
		driver.close();
	}
	
}