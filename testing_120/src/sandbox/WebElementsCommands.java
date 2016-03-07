package sandbox;
	import java.sql.Driver;
	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.firefox.FirefoxDriver;

public class WebElementsCommands {
	
	public static void main(String[] args) throws InterruptedException {
	
		WebDriver driver = new FirefoxDriver();
		
		String URL = "http://englishteststore.net/index.php?option=com_content&view=article&id=14317&Itemid=557";
		driver.get(URL);
		
		Integer PageSource = driver.getPageSource().length();
		System.out.println("Page source length: "+PageSource);
		Thread.sleep(5000);
		
		// check if Carambola SCRIPT was in the HTML  
		boolean LayerStatus = driver.findElement(By.id("InContentScript0")).isEnabled();
		if(LayerStatus){
			System.out.println("cbola SCRIPT was loaded succefuly inside the HTML");
		}else{
			System.out.println("cbola SCRIPT WASN'T LOADED...bummer. get Tomer fast");
		}
		
		// check if carambola centerWrapper got opened    
		boolean CbolaScriptStatus = driver.findElement(By.id("InContent-container-centerWrapper0")).isDisplayed();
		if(CbolaScriptStatus){
			System.out.println("cbola centerWrapper was loaded succefuly inside the HTML");
		}else{
			System.out.println("cbola centerWrapper WASN'T LOADED...bummer. get Tomer fast");
		}
		
		// check TEXT 
		String text_Question = "Question";
		String ScoreTitle = driver.findElement(By.className("cbolaContent-scoreTitle")).getText();
		System.out.println("ref text is: " + text_Question);
		System.out.println("text on screen is: " + ScoreTitle);
		
		String text_yourScore = "Your score";
		String ScoreUnit = driver.findElement(By.className("cbolaContent-scoreUnit")).getText();
		System.out.println("ref text is: " + text_yourScore);
		System.out.println("text on screen is: " + ScoreUnit);
		
		
			// score title
		if(new String(text_Question).equals(ScoreTitle)){   //ScoreTitle == text_Question why doesn't it works?...
			System.out.println("the score_title matches the ref text");
		}else{
			System.out.println("AARRR-there is NO match between the score title texts");
		}
			// score unit 
		if(new String(text_yourScore).equals(ScoreUnit)){   
			System.out.println("the score_units matches the ref text");
		}else{
			System.out.println("AARRR-there is NO match between the unit_score texts");
		}
		// check if carambola Lower AD was loaded
		boolean LowerAdStatus = driver.findElement(By.className("cbolaBanner0_4")).isDisplayed();
		if(LayerStatus){
			System.out.println("cbola lower AD was loaded succefuly and displayed on screen");
		}else{
			System.out.println("cbola lower AD WASN'T LOADED...bummer. get Tomer fast");
		}
		
		driver.close();
		}
	
		
}


