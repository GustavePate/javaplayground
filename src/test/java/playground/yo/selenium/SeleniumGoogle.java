package playground.yo.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.assertj.core.api.Assertions.*;

public class SeleniumGoogle {

	
	public static WebDriver driver;
	
	@BeforeClass
	public static void setupContext(){
        driver = new FirefoxDriver();
	}
	
	@Test
	public void testGoogle() throws InterruptedException{
        driver.get("http://www.google.com");
        assertThat(driver.getTitle()).isEqualTo("Google");
        //by id
        driver.findElement(By.id("lst-ib")).sendKeys("arkea");
        //css selector
        driver.findElement(By.cssSelector("button[value=Rechercher]")).click();
		
        //obligé d'attendre :(
        Thread.sleep(2000);
        //count results
        assertThat(driver.findElements(By.cssSelector("div.g")).size()).isGreaterThan(5);
        
        
        
	}
	
	@Test
	public void testSelenium(){
        driver.get("http://seleniumhq.org");
	}
	
	@AfterClass
	public static void cleanup(){
        driver.quit();
	}
	
}
