public class CmdRequestEquipment extends RecordedCommand {
    private RequestRecord equipmentRequest;
    private Member m;
    private EquipmentSet equipSet;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 5)
                throw new ExInsufficientArguments();

            Club club = Club.getInstance();

            // Find member by their id
            m = club.checkMemberExistById(cmdParts[1]);
            // Find equipment by their equipment code
            Equipment e = club.checkEquipmentExistByCode(cmdParts[2]);

            // Cannot find any one of them then throw exception
            if (m == null)
                throw new ExMemberNotFound();
            if (e == null)
                throw new ExEquipmentNotFound();

            // If the date in the command is not valid then it will throw the exception
            SystemDate.getInstance().checkDateFormat(cmdParts[3]);
            // Instantiate day object if it is valid
            Day startDay = new Day(cmdParts[3]);

            // Get the number of days for the request
            int numberOfDays = Integer.parseInt(cmdParts[4]);
            if (numberOfDays < 1)
                throw new ExInvalidNumberOfDays();
            // Create the end day object if able to get the number of day
            Day endDay = startDay.advanaceDay(Integer.parseInt(cmdParts[4]));

            equipSet = e.getCanRequestEquipmentSet(m, startDay, endDay);
            if (equipSet == null)
                throw new ExNoAvilavleEquipmentSet();

            equipmentRequest = new RequestRecord(m, equipSet, startDay, endDay);
            m.requestEquipment(equipSet, equipmentRequest);
            clearRedoList();
            addUndoCommand(this);

            System.out.printf("%s %s requests %s (%s) for %s to %s\n",
                               m.getID(), m.getName(), equipSet.getSetNumber(), e.getEquipmentName(),startDay, endDay);
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExMemberNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExEquipmentNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExInvalidDateFormat e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please provide an integer for the number of days.");
        } catch (ExInvalidNumberOfDays e) {
            System.out.println(e.getMessage());
        } catch (ExOverlapPeriods e) {
            System.out.println(e.getMessage());
        } catch (ExNoAvilavleEquipmentSet e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        m.cancelRequestEquipment(equipmentRequest);
        equipSet.cancelRequestRecord(equipmentRequest);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        try {
            m.requestEquipment(equipSet, equipmentRequest);
            addUndoCommand(this);
        } catch (ExOverlapPeriods e) {
            System.out.println(e.getMessage());
        }
    }

}
