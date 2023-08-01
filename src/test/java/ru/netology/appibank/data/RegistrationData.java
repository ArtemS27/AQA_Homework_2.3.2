package ru.netology.appibank.data;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class RegistrationData {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();


    static void setUpUser(UserInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String name = faker.name().firstName();
        return name;
    }

    public static String generatePassword(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String password = faker.funnyName().name();
        return password;
    }

    public static String generateStatus(String locale) {
        String[] statuses = {"active", "blocked"};
        Random random = new Random();
        String status = statuses[random.nextInt(statuses.length)];
        return status;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String status) {
            UserInfo user = new UserInfo(
                    generateName("en"),
                    generatePassword("en"),
                    status
            );
            return user;
        }

        public static UserInfo getActiveUser(String status) {
            UserInfo activeUser = generateUser(status);
            setUpUser(activeUser);

            return activeUser;
        }
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }
}
