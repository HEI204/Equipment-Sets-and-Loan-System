public class ExOverlapPeriods extends Exception {
    public ExOverlapPeriods() {
        super("The period overlaps with a current period that the member requests the equipment.");
    }

    public ExOverlapPeriods(String message) {
        super(message);
    }
}
