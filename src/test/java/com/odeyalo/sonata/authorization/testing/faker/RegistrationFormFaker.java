package com.odeyalo.sonata.authorization.testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RegistrationFormFaker {
    private static final RegistrationFormCreator creator = new RegistrationFormCreator();
    private static final Faker faker = new Faker();

    public static RegistrationFormFaker getInstance() {
        return new RegistrationFormFaker();
    }

    public static RegistrationForm create() {
        prepareFakeData();
        return creator.create();
    }

    private static void prepareFakeData() {
        creator
                .setUsername(faker.internet().emailAddress())
                .setPassword(faker.internet().password())
                .setBirthdate(getBirthdate())
                .setEnableNotification(faker.random().nextBoolean())
                .setGender(getGender());
    }

    private static String getGender() {
        Integer num = faker.random().nextInt(0, 2);
        if (num == 0) return Gender.MALE;
        if (num == 1) return Gender.FEMALE;
        return Gender.NONE;
    }

    private static LocalDate getBirthdate() {
        Date birthday = faker.date().birthday(10, 100);
        return birthday.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public RegistrationFormFaker overrideUsername(String username) {
        creator.setUsername(username);
        return this;
    }

    public RegistrationFormFaker overridePassword(String password) {
        creator.setPassword(password);
        return this;
    }

    public RegistrationFormFaker overrideGender(String gender) {
        creator.setGender(gender);
        return this;
    }

    public RegistrationFormFaker overrideBirthdate(LocalDate birthdate) {
        creator.setBirthdate(birthdate);
        return this;
    }

    public RegistrationFormFaker overrideEnableNotification(boolean flag) {
        creator.setEnableNotification(flag);
        return this;
    }

    public RegistrationFormFaker disabledNotification() {
        return overrideEnableNotification(false);
    }

    public RegistrationFormFaker enabledNotification() {
        return overrideEnableNotification(true);
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    static class RegistrationFormCreator {
        String username;
        String password;
        String gender;
        LocalDate birthdate;
        boolean enableNotification;

        public RegistrationForm create() {
            return RegistrationForm.of(username, password, gender, birthdate, enableNotification);
        }
    }

    public static class Gender {
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";
        public static final String NONE = "NONE";

    }
}
