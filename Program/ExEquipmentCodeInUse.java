public class ExEquipmentCodeInUse extends Exception {
    public ExEquipmentCodeInUse() {
        super("Equipment code already in use.");
    }

    public ExEquipmentCodeInUse(String message) {
        super(message);
    }
}
