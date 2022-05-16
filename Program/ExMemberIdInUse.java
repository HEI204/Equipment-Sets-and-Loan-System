public class ExMemberIdInUse extends Exception {
    public ExMemberIdInUse() {
        super("Member ID already in use.");
    }

    public ExMemberIdInUse(String message) {
        super(message);
    }
}
