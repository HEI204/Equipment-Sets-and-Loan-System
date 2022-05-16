public class BorrowRecord implements Comparable<BorrowRecord> {
    private Member m;
    private EquipmentSet equipSet;
    private Day borrowStartDay;
    private Day borrowEndDay;

    public BorrowRecord(Member m, EquipmentSet eSet, Day borrowStartDay, Day borrowEndDay) {
        this.m = m;
        this.equipSet = eSet;
        this.borrowStartDay = borrowStartDay;
        this.borrowEndDay = borrowEndDay;
    }

    public String getMemberBorrowInfo() {
        return this.m.getID() + " " + this.m.getName();
    }

    public String getBorrowedEquipmemtSetWithMemberInfo() {
        return this.equipSet.getSetNumber() + "(" + this.m.getID() + ")";
    }

    public Day getBorrowStartDay() {
        return this.borrowStartDay.clone();
    }

    public Day getBorrowEndDay() {
        return this.borrowEndDay.clone();
    }

    public boolean findMemberBorrow(Member m) {
        return this.m == m;
    }

    public boolean checkSameSetOfEquipmentSet(EquipmentSet es) {
        return equipSet.checkSameSetOfEquipment(es);
    }

    

    public boolean checkOverlapPeriodsWithBorrow(Day startDay, Day endDay) {
        // No overlapping if (new request end day is earlier than existing borrow start day) or (new request start day is later than existing borrow end day)
        if (this.borrowEndDay.compareTo(startDay) == -1 || this.borrowStartDay.compareTo(endDay) == 1)
            return false;
        return true;
        
    }

    @Override
    public int compareTo(BorrowRecord anotherRequest) {
        if (this.equipSet.getSetNumber().compareTo(anotherRequest.equipSet.getSetNumber()) == 1 || 
            this.borrowStartDay.compareTo(anotherRequest.borrowStartDay) == 1)
            return 1;
        else
            return -1;
    }

    public String getBorrowPeriod() {
        return this.borrowStartDay + " to " + this.borrowEndDay;
    }

    public String getBorrowDetails() {
        return String.format("%s (%s) for %s", equipSet.getSetNumber(), equipSet.getEquipmentSetName(), getBorrowPeriod());
    }

}
