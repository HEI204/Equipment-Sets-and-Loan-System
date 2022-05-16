public class CmdStartNewDay extends RecordedCommand {
    private String oldDate;
    private String newDate;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 2)
                throw new ExInsufficientArguments();

            // Check the new system date is valid, if not then throw an exception
            SystemDate.checkValidSystemDate(cmdParts[1]);

            SystemDate sd = SystemDate.getInstance();
            oldDate = sd.toString();
            newDate = cmdParts[1];
            sd.set(newDate);

            clearRedoList();
            addUndoCommand(this);
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExInvalidDateFormat e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        SystemDate sd = SystemDate.getInstance();
        sd.set(oldDate);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        SystemDate sd = SystemDate.getInstance();
        sd.set(newDate);
        addUndoCommand(this);
    }

}
