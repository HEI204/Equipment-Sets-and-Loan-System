public class CmdAddSetOfEquipment extends RecordedCommand {
    private Equipment e;
    private EquipmentSet equipSet;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 2)
                throw new ExInsufficientArguments();

            // check the equipment by the equipment code and return the Equipment object
            e = Club.getInstance().checkEquipmentExistByCode(cmdParts[1]);
            // If no such equipment code
            if (e == null)
                throw new ExEquipmentNotFound();

            // Instantiate EquipmentSet object with passing the corresponding set number
            equipSet = new EquipmentSet(e.getEquipmentCode() + "_" + (e.getEquipmentSetSize() + 1), e.getEquipmentName());

            e.addIntoEqiupmentSet(equipSet);

            clearRedoList();
            addUndoCommand(this);
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExEquipmentNotFound e1) {
            System.out.println("Missing record for Equipment " + cmdParts[1] + ".  Cannot mark this item arrival.");
        }
    }

    @Override
    public void undoMe() {
        e.removeFromEquipmentSet(equipSet);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        e.addIntoEqiupmentSet(equipSet);
        addUndoCommand(this);
    }

}
