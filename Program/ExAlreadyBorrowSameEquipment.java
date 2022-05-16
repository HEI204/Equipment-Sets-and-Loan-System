public class ExAlreadyBorrowSameEquipment extends Exception {
    public ExAlreadyBorrowSameEquipment() {
        super("The member is currently borrowing a set of this equipment. He/she cannot borrow one more at the same time.");
    }

    public ExAlreadyBorrowSameEquipment(String message) {
        super(message);
    }
}
