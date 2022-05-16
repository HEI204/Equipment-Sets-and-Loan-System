import java.util.ArrayList;
import java.util.Collections;

public class EquipmentSet implements Comparable<EquipmentSet> {
    private String setNumber;
    private String equipmentName;
    private BorrowRecord borrow;
    private ArrayList<RequestRecord> requestRecords;

    public EquipmentSet() {
    }

    public EquipmentSet(String setNumber, String equipmentName) {
        this.setNumber = setNumber;
        this.equipmentName = equipmentName;
        this.borrow = null;
        this.requestRecords = new ArrayList<>();
    }

    public String getSetNumber() {
        return this.setNumber;
    }

    public String getEquipmentSetName() {
        return this.equipmentName;
    }

    public String loanPeriodDetails() {
        return borrow.getBorrowPeriod();
    }

    public String memberInfoForBorrow() {
        return this.borrow.getBorrowedEquipmemtSetWithMemberInfo();
    }

    public String borrowedStatus() {
        return String.format("Current status: %s borrows for %s", this.borrow.getMemberBorrowInfo(),
                loanPeriodDetails());
    }

    public void borrowBy(BorrowRecord equipmentBorrowRecord) {
        borrow = equipmentBorrowRecord;
    }

    public void cancelBorrowedStatus() {
        borrow = null;
    }

    public boolean checkAvailable() {
        return borrow == null;
    }

    public boolean HasBorrowedBy(Member m) {
        return this.borrow.findMemberBorrow(m);
    }

    public boolean checkSameSetOfEquipment(EquipmentSet eSet) {
        return this.equipmentName.equals(eSet.equipmentName);
    }

    public boolean canRequest(Member m, Day requestStartDay, Day requestEndDay) throws ExOverlapPeriods {
        // Check if the new request period is not overlap with the current borrow period
        if (!checkAvailable() && borrow.checkOverlapPeriodsWithBorrow(requestStartDay, requestEndDay))
            return false;

        for (RequestRecord r : requestRecords) {
            // Check the new request period is not overlap with the member preivous
            // requested record
            if (r.findMemberRequest(m)) {
                if (r.checkOverlapPeriods(requestStartDay, requestEndDay))
                    throw new ExOverlapPeriods("The period overlaps with a current period that the member borrows / requests the equipment.");
            }
            // Check the new request period is not overlap with all other previous requested
            // period for this equipment set
            if (!r.checkOverlapPeriods(requestStartDay, requestEndDay))
                continue;
            else
                return false;
        }
    
        return true;
    }

    public void addRequest(RequestRecord r) {
        this.requestRecords.add(r);
        Collections.sort(requestRecords);
    }

    public void cancelRequestRecord(RequestRecord r) {
        this.requestRecords.remove(r);
    }

    public boolean hasRequestRecord() {
        return requestRecords.size() != 0;
    }

    public boolean hasRequestedBy(Member mToFind) {
        for (RequestRecord r : requestRecords)
            if (r.findMemberRequest(mToFind))
                return true;
        return false;
    }

    public String requestedStatus() {
        String status = "";
        boolean first_output = true;
        for (RequestRecord r : requestRecords) {
            if (!first_output)
                status += ", ";

            status += (r.getRequestPeriod());
            first_output = false;
        }
        return "Requested period(s): " + status;
    }

    public String requestedRecordBy(Member m) {
        String record = "";
        for (RequestRecord r : requestRecords) {
            if (r.findMemberRequest(m)) {
                record += r.getRequestStartDay() + " to " + r.getRequestEndDay() + ",";
            }
        }

        return record;
    }

    @Override
    public int compareTo(EquipmentSet anotherEquipmentSet) {
        return this.setNumber.compareTo(anotherEquipmentSet.setNumber);
    }
}
