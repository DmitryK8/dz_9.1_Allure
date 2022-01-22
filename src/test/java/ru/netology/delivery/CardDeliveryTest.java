package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(false)
                .savePageSource(true)
        );
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void ShouldPlanAndReplanMeeting() {
        Configuration.holdBrowserOpen = true;
        val validUser = DataGenerator.Registration.generateUser();
        val daysAddForOneMeeting = 4;
        val oneMeetingDate = DataGenerator.generateDate(daysAddForOneMeeting);
        val daysAddForTwoMeeting = 7;
        val twoMeetingDate = DataGenerator.generateDate(daysAddForTwoMeeting);
        $("[data-test-id=city] .input__control").setValue(validUser.getCity());
        $("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id=date] .input__control").setValue(oneMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $("[data-test-id=success-notification]>.notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + oneMeetingDate));
        $("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id=date] .input__control").setValue(twoMeetingDate);
        $(".button__text").click();
        $(withText("Необходимо подтверждение")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button").click();
        $("[data-test-id=success-notification]>.notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + twoMeetingDate));
    }
}