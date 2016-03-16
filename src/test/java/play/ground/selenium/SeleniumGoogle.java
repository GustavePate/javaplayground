package play.ground.selenium;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class SeleniumGoogle {

	public static WebDriver driver;

	@BeforeClass
	public static void setupContext() {
		driver = SeleniumUtils.getDriver();
	}

	@Before
	public void go_to_google() {

		driver.get("about:home");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.navigate().to("https://www.google.com/ncr");
		wait.until(ExpectedConditions.titleIs("Google"));
	}

	@Test
	public void testGoogle_Fail() {

		assertThat(driver.getTitle()).isEqualTo("Google");

		// by id: remplir le champ de recherche
		driver.findElement(By.id("lst-ib")).sendKeys("arkea");

		// css selector: cliquer sur le bouton rechercher
		driver.findElement(By.cssSelector("button[value=Search]")).click();

		// css selector: vérifier le nombre de résultats
		assertThat(driver.findElements(By.cssSelector("div.g")).size()).isGreaterThan(5);
	}

	@Test
	public void testGoogle() throws InterruptedException {
		assertThat(driver.getTitle()).isEqualTo("Google");
		// by id
		driver.findElement(By.id("lst-ib")).sendKeys("arkea");
		// css selector
		driver.findElement(By.cssSelector("button[value=Search]")).click();

		// obligé d'attendre, super moche quid de 2001 :(
		Thread.sleep(2000);

		// count results
		assertThat(driver.findElements(By.cssSelector("div.g")).size()).isGreaterThan(5);
	}

	@Test
	public void testGoogle2() throws InterruptedException {

		// Gestion globale du timeout
		// va impacter les vérifications d'absence d'éléments

		assertThat(driver.getTitle()).isEqualTo("Google");

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// by id
		driver.findElement(By.id("lst-ib")).sendKeys("arkea");
		// css selector
		driver.findElement(By.cssSelector("button[value=Search]")).click();

		// count results
		assertThat(driver.findElements(By.cssSelector("div.g")).size()).isGreaterThan(5);
	}

	@Test
	public void testGoogle3() throws InterruptedException {

		driver.navigate().to("https://www.google.com/ncr");

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.titleIs("Google"));

		assertThat(driver.getTitle()).isEqualTo("Google");

		// by id
		driver.findElement(By.name("q")).sendKeys("arkea");
		// css selector
		driver.findElement(By.cssSelector("button[value=Search]")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.g")));

		// count results
		assertThat(driver.findElements(By.cssSelector("div.g")).size()).isGreaterThan(5);
	}

	@Test
	public void testSelenium() {
		driver.get("http://seleniumhq.org");
	}

	@After
	public void resetDriver() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void cleanup() {
		driver.quit();
	}

}
