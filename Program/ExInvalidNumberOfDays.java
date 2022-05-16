public class ExInvalidNumberOfDays extends Exception {

    public ExInvalidNumberOfDays() {
        super("The number of days must be at least 1.");
    }

    public ExInvalidNumberOfDays(String message) {
        super(message);
    }

}
