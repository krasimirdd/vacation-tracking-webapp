package util;

public class ValidationUtil {

    public static boolean validateUsername(String username) {
        String regularExpression = "^[[A-Z]|[a-z]][[A-Z]|[a-z]|\\d|[_]]{4,29}$";
        return username.matches(regularExpression);
    }

    public static boolean validatePassword(String password) {
        String regularExpression = "^(?=\\S+$).{8,}$";
        return password.matches(regularExpression);
    }
}
