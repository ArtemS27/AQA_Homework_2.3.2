package ru.netology.appibank.data;


import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

public class RegistrationData {
    private String login;
    private String password;
    private String status;

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

        public static UserInfo generateUser(String locale) {
            UserInfo user = new UserInfo(
                    generateName(locale),
                    generatePassword(locale),
                    generateStatus(locale)
            );
            return user;
        }
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }

    public RegistrationData(String login, String password, String status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }
}
