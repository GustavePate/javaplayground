package play.ground.selenium;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import org.junit.Test;
import org.openqa.selenium.By;
import static org.assertj.core.api.Assertions.*;

public class SelenideGoogle {

	@Test
	public void testGoogle() {

		// go to google
		open("http://google.com/ncr");
		assertThat(title()).isEqualTo("Google");

		// select by name
		$(By.name("q")).val("des petits chats").pressEnter();

		// by css: au moins un résultat est affiché
		$("#ires .g").shouldBe(visible);
		assertThat($$("#ires .g").stream().count()).isGreaterThan(5);
	}

	public void notes() {

		// $(byText("Next")).waitUntil(visible, 2000);

	}
}