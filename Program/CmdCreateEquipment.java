public class CmdCreateEquipment extends RecordedCommand {
    private Equipment e;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 3)
                throw new ExInsufficientArguments();

            // Check whether the equipment code are already used by other existing equipment
            // if already in use, then throw exception
            if (Club.getInstance().checkEquipmentExistByCode(cmdParts[1]) != null)
                throw new ExEquipmentCodeInUse();

            e = new Equipment(cmdParts[1], cmdParts[2]);

            clearRedoList();
            addUndoCommand(this);
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExEquipmentCodeInUse e1) {
            System.out.println("Equipment code already in use: " + cmdParts[1] + " "
                    + Club.getInstance().checkEquipmentExistByCode(cmdParts[1]).getEquipmentName());
        }
    }

    @Override
    public void undoMe() {
        Club.getInstance().removeEquipment(e);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        Club.getInstance().addEqiupment(e);
        addUndoCommand(this);
    }
}
