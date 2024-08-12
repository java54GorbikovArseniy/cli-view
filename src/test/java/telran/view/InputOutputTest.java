package telran.view;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

record User(String username, String password, LocalDate dateLastLogin, String phoneNumber, int numberOfLogins) {
}

public class InputOutputTest {
    InputOutput io = new SystemInputOutput();

    @Test
    void readObjectTest() {
        User user = io.readObject("Enter user in format  <username>#<password>#<dateLastLogin#<phoneNumber>#" +
                "<numberOfLogins>", "Wrong user input format", str -> {
            String[] tokens = str.split("#");
            return new User(tokens[0],
                    tokens[1],
                    LocalDate.parse(tokens[2]),
                    tokens[3],
                    Integer.parseInt(tokens[4]));
        });
        io.writeLine(user);
    }

@Test
void readUserByFields() {
        String username = io.readStringPredicate("Enter username",
                "Wrong username. Username must be at least 6 ascii letters - first letter Capital, others Lower case",
                str -> str.matches("[A-Z][a-z]{5,}"));
        String password = io.readStringPredicate("Enter password",
                "Password must cotains at least 8 symbol, at least one : capital letter, lower case letter, digit, symbol from \"#$*$%\"",
                s -> s.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#\\$*%]).{8,}$"));
        LocalDate dateLastLogin = io.readIsoDateRange("Enter last login date", "Date wrong",
                LocalDate.MIN, LocalDate.now().plusDays(1));
        String phoneNumber = io.readStringPredicate("Enter phone number", "Wrong phone number format",
                s -> s.matches("^(\\+972|0)([23489]|5[0-9])\\d{7}$"));
        int numberOfLogins = io.readNumberRange("Enter number of logins", "number must be positive",
                1.0, Double.MAX_VALUE).intValue();
        io.writeLine(new User(username, password, dateLastLogin, phoneNumber, numberOfLogins));
    }
}
