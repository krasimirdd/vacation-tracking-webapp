package util;

public class Messages {
    public static class Email {
        public static final String EMAIL_SENDING = "Email sending to refer user";
        public static final String OVERBOOKED = "More than 40% of the users associated with your team have a vacation at the same time.";
        public static final String OVERBOOKED_FLASH = OVERBOOKED + "Request has been automatically rejected." + EMAIL_SENDING;
        public static final String VACATION_REQUEST = "Request for vacation has arrived in you inbox";
        public static final String SUBJECT = "Vacation request";
        public static final String STATUS = "Your vacation request has been ";
        public static final String REASON = "Reason : ";
        public static final String RESTRICT_ANSWER_BACK_MESSAGE = "Hello, please don't answer back on this email";
    }

    public static class Halt {
        public static final String USER_RESTRICTED = "Must be logged user to see the content";
        public static final String ADMIN_RESTRICTED = "Must be logged admin to see the content";
        public static final String FORBIDDEN = "Access denied!";
    }

    public static class Credentials {
        public static final String CONFIRM_PASSWORD_ERROR = "Passwords mismatch";
        public static final String SAME_CREDENTIALS_ERROR = "User with same credentials already is in the system. Try again!";
        public static final String WRONG_CREDENTIALS = "Wrong username or password";
    }

    public static class UserPage {
        public static final String ACCEPTED_REQUEST = "Admin will review you application";
        public static final String INVALID_REQUEST = "Invalid request";
        public static final String CANCELED = "Successfully canceled";
        public static final String UNABLE_TO_CANCEL = "Cannot cancel already consumed vacation";
    }

    public static class AdminPage {
        public static final String NO_REQUESTS = "No registered users in your team.";
        public static final String NO_VACATION = "No such vacation";
    }

    public static class HTML {
        public static final String NEW_LINE = "<br>";
        public static final String FLASH_MESSAGE_KEY = "flash_message";
    }

}
