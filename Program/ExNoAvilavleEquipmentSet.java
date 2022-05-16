public class ExNoAvilavleEquipmentSet extends Exception {
    public ExNoAvilavleEquipmentSet() {
        super("There is no available set of this equipment for the command.");
    }

    public ExNoAvilavleEquipmentSet(String message) {
        super("message");
    }
}
