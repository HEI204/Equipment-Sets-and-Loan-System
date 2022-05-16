public class CmdBorrowEquipment extends RecordedCommand {
    private BorrowRecord equipmentBorrowRecord;
    private EquipmentSet equipSet;
    private Member m;
    private int loanDuration = 7;

    @Override
    public void execute(String[] cmdParts) {
        try {
            // validate basic command : borrow [member ID] [equipmemnt code]
            if (cmdParts.length < 3)
                throw new ExInsufficientArguments();
            // validate advance version of command: borrow [member ID] [equipmemnt code]
            // [loan duration]
            if (cmdParts.length > 3 && cmdParts.length < 4)
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

            // Get the loan duration if it is the advance version command
            if (cmdParts.length == 4) {
                loanDuration = Integer.parseInt(cmdParts[3]);
                if (loanDuration < 1)
                    throw new ExInvalidNumberOfDays();
            }

            // Get the loan start and end day
            Day startDay = SystemDate.getInstance().clone();
            Day endDay = startDay.advanaceDay(loanDuration);

            // Return the EquipmentSet from the Equipment which is allow member to borrow
            // If there is overlap periods then throw exception
            equipSet = e.getCanBorrowEquipmentSet(m, startDay, endDay);

            // If it is null means there is no available equipment set then throw exception
            if (equipSet == null)
                throw new ExNoAvilavleEquipmentSet();

            equipmentBorrowRecord = new BorrowRecord(m, equipSet, startDay, endDay);

            m.borrowEquipment(equipSet, equipmentBorrowRecord);
            clearRedoList();
            addUndoCommand(this);

            System.out.printf("%s %s borrows %s (%s) for %s\n",
                               m.getID(), m.getName(), equipSet.getSetNumber(), e.getEquipmentName(), equipSet.loanPeriodDetails());
            System.out.println("Done. ");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExMemberNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExEquipmentNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExNoAvilavleEquipmentSet e) {
            System.out.println(e.getMessage());
        } catch (ExAlreadyBorrowSameEquipment e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please provide an integer for the number of days.");
        } catch (ExInvalidNumberOfDays e) {
            System.out.println(e.getMessage());
        } catch (ExOverlapPeriods e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        m.cancelBorrowEquipment(equipmentBorrowRecord);
        equipSet.cancelBorrowedStatus();
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        try {
            m.borrowEquipment(equipSet, equipmentBorrowRecord);
            addUndoCommand(this);
        } catch (ExAlreadyBorrowSameEquipment e) {
            System.out.println(e.getMessage());
        } catch (ExOverlapPeriods e) {
            System.out.println(e.getMessage());
        }
    }

}
