public class CmdRegister extends RecordedCommand {
    private Member m;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 3)
                throw new ExInsufficientArguments();

            // Check whether the new member id is exist before or not, 
            // if exist then throw the exception
            if (Club.getInstance().checkMemberExistById(cmdParts[1]) != null)
                throw new ExMemberIdInUse();

            m = new Member(cmdParts[1], cmdParts[2]);
            clearRedoList();
            addUndoCommand(this);
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExMemberIdInUse e) {
            System.out.printf("Member ID already in use: %s %s\n", cmdParts[1], Club.getInstance().checkMemberExistById(cmdParts[1]).getName());
        }
    }

    @Override
    public void undoMe() {
        Club.getInstance().removeMember(m);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        Club.getInstance().addMember(m);
        addUndoCommand(this);
    }

}
