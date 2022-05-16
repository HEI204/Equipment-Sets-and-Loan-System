import java.util.ArrayList;
import java.util.Collections;

public class Member implements Comparable<Member> {
    private String id;
    private String name;
    private Day joinDate;
    private ArrayList<BorrowRecord> borrowedRecords;
    private ArrayList<RequestRecord> requestedRecords;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        borrowedRecords = new ArrayList<>();
        requestedRecords = new ArrayList<>();
        joinDate = SystemDate.getInstance().clone();
        Club.getInstance().addMember(this);
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.id;
    }

    public boolean checkIdInUse(String anotherId) {
        return this.id.equals(anotherId);
    }

    @Override
    public String toString() {
        return String.format("%-5s%-9s%11s%7d%13d", id, name, joinDate, borrowedRecords.size(),
                requestedRecords.size());
    }

    // When a member borrow an equipment, check and make sure he/she has not borrowed the same equipment before
    // and also the borrow period is not overlap with previous requested record
    public void borrowEquipment(EquipmentSet eSetToBeBorrow, BorrowRecord equipmentBorrowRecord) throws ExAlreadyBorrowSameEquipment, ExOverlapPeriods {
        // Avoid borrow two or more sets of the same equipment
        if (checkBorrowAlready(eSetToBeBorrow))
            throw new ExAlreadyBorrowSameEquipment();

        if (requestedRecords.size() > 0) {
            Day borrowStartDay = equipmentBorrowRecord.getBorrowStartDay();
            Day borrowEndDay = equipmentBorrowRecord.getBorrowEndDay();
            for (RequestRecord r : requestedRecords) {
                if (r.findRequestedEquipSet(eSetToBeBorrow))
                    if (r.checkOverlapPeriods(borrowStartDay, borrowEndDay))
                        throw new ExOverlapPeriods();
            }
            eSetToBeBorrow.borrowBy(equipmentBorrowRecord);
            this.borrowedRecords.add(equipmentBorrowRecord);
        }

        else {
            eSetToBeBorrow.borrowBy(equipmentBorrowRecord);
            this.borrowedRecords.add(equipmentBorrowRecord);
        }

        Collections.sort(borrowedRecords);
    }

    public void cancelBorrowEquipment(BorrowRecord b) {
        borrowedRecords.remove(b);
    }

    private boolean checkBorrowAlready(EquipmentSet eSetToBeBorrow) {
        for (BorrowRecord b : borrowedRecords)
            if (b.checkSameSetOfEquipmentSet(eSetToBeBorrow))
                return true;
        return false;
    }

    // When a member try to request an equipment, check and make sure the request is not overlap with
    // he/she previous request/borrow record
    public void requestEquipment(EquipmentSet equipSet, RequestRecord equipRequest) throws ExOverlapPeriods {
        for (BorrowRecord b : borrowedRecords) {
            if (b.checkSameSetOfEquipmentSet(equipSet))
                if (equipRequest.checkOverlapPeriods(b.getBorrowStartDay(), b.getBorrowEndDay()))
                    throw new ExOverlapPeriods("The period overlaps with a current period that the member borrows / requests the equipment.");
        }

        for (RequestRecord r : requestedRecords) {
            if (r.findRequestedEquipSet(equipSet))
                if (r.checkOverlapPeriods(equipRequest.getRequestStartDay(), equipRequest.getRequestEndDay()))
                    throw new ExOverlapPeriods("The period overlaps with a current period that the member borrows / requests the equipment.");
        }

        equipSet.addRequest(equipRequest);
        this.requestedRecords.add(equipRequest);
    }

    public void cancelRequestEquipment(RequestRecord equipRequest) {
        this.requestedRecords.remove(equipRequest);
    }

    @Override
    public int compareTo(Member another) {
        return this.id.compareTo(another.id);
    }

    public static String getListingHeader() {
        return String.format("%-5s%-9s%11s%11s%13s", "ID", "Name", "Join Date", "#Borrowed", "#Requested");
    }

    // Used for listMemberStatus
    // generate the each record for borrow & request for that member 
    public String getStatus() {
        if (borrowedRecords.size() > 0 || requestedRecords.size() > 0) {
            String info = "";
            if (borrowedRecords.size() > 0) {
                for (BorrowRecord b : borrowedRecords) {
                    info += String.format("- borrows %s\n", b.getBorrowDetails());
                }
            }

            if (requestedRecords.size() > 0) {
                for (RequestRecord r : requestedRecords) {
                    // EquipmentSet eSet = e.getEquipmentSetRequestBy(this);
                    // String[] recordOfRequestTime = eSet.requestedRecordBy(this).split(",");
                    info += String.format("- requests %s\n", r.getRequestDetails());
                }
            }
            return info;
        }

        else
            return "No record.\n";
    }
}