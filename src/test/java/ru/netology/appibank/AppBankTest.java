package ru.netology.appibank;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.appibank.data.RegistrationData;

import static io.restassured.RestAssured.given;
import static com.codeborne.selenide.Selenide.*;

public class AppBankTest {

    @BeforeEach
    void setup(){
        open("http://localhost:9999");
    }
    @Test
    public void shouldLogIn() {
        RegistrationData.UserInfo user = RegistrationData.Registration.getActiveUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        $(".heading").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    public void shouldThrowNotificationIfUserNotRegistered(){
        RegistrationData.UserInfo user = RegistrationData.Registration.generateUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
    @Test
    public void shouldThrowNotificationIfLoginInvalid() {
        RegistrationData.UserInfo user = RegistrationData.Registration.getActiveUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(RegistrationData.generateName("en"));
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldThrowNotificationIfPasswordInvalid() {
        RegistrationData.UserInfo user = RegistrationData.Registration.getActiveUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(RegistrationData.generatePassword("en"));
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldThrowNotificationIfUserIsBlocked() {
        RegistrationData.UserInfo user = RegistrationData.Registration.getActiveUser("blocked");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Пользователь заблокирован"));
    }
}
