package play.ground.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumUtils {

	public static WebDriver getDriver() {

		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile myprofile = profile.getProfile("selenium");
		WebDriver driver = new FirefoxDriver(myprofile);

		return driver;
	}

}
