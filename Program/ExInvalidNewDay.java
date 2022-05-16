public class ExInvalidNewDay extends Exception {
    public ExInvalidNewDay() {
        super("Invalid new day.");
    }

    public ExInvalidNewDay(String message) {
        super(message);
    }
}
