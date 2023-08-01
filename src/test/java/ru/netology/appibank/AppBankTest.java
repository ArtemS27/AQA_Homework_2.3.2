package ru.netology.appibank;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import ru.netology.appibank.data.RegistrationData;

import static io.restassured.RestAssured.given;
import static com.codeborne.selenide.Selenide.*;

public class AppBankTest {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();


    static void setUpActiveUser() {
        given()
                .spec(requestSpec)
                .body(new RegistrationData("vasya", "password", "active"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    static void setUpBlockedUser() {
        given()
                .spec(requestSpec)
                .body(new RegistrationData("vasya", "password", "blocked"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldLogIn() {
        setUpActiveUser();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue("vasya");
        form.$("[data-test-id=password] input").setValue("password");
        form.$("[data-test-id=action-login]").click();
        $(".heading").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    public void shouldThrowNotificationIfLoginInvalid() {
        setUpActiveUser();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue("petya");
        form.$("[data-test-id=password] input").setValue("password");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldThrowNotificationIfPasswordInvalid() {
        setUpActiveUser();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue("vasya");
        form.$("[data-test-id=password] input").setValue("invalid");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldThrowNotificationIfUserIsBlocked() {
        setUpBlockedUser();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue("vasya");
        form.$("[data-test-id=password] input").setValue("password");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Пользователь заблокирован"));
    }
}
