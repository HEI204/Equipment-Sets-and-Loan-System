public class RequestRecord implements Comparable<RequestRecord> {
    private Member m;
    private EquipmentSet equipSet;
    private Day requestStartDay;
    private Day requestEndDay;

    public RequestRecord(Member m, EquipmentSet equipSet, Day requestStartDay, Day requestEndDay) {
        this.m = m;
        this.equipSet = equipSet;
        this.requestStartDay = requestStartDay;
        this.requestEndDay = requestEndDay;
    }

    public Day getRequestStartDay() {
        return this.requestStartDay.clone();
    }

    public Day getRequestEndDay() {
        return this.requestEndDay.clone();
    }

    public boolean findMemberRequest(Member m) {
        return this.m == m;
    }

    public boolean findRequestedEquipSet(EquipmentSet es) {
        return equipSet == es;
    }

    @Override
    public int compareTo(RequestRecord anotherRequest) {
        if (this.equipSet.getSetNumber().compareTo(anotherRequest.equipSet.getSetNumber()) == 1 ||
            this.requestStartDay.compareTo(anotherRequest.requestStartDay) == 1)
            return 1;
        else
            return -1;
    }

    public String getRequestPeriod() {
        return this.requestStartDay + " to " + this.requestEndDay;
    }

    public String getRequestDetails() {
        return String.format("%s (%s) for %s", equipSet.getSetNumber(), equipSet.getEquipmentSetName(),
                this.getRequestPeriod());
    }

    public boolean checkOverlapPeriods(Day startDay, Day endDay) {
        // No overlapping if (existing end day is earlier than new request start day) or (existing start day is later than new request end day)
        if (this.requestEndDay.compareTo(startDay) == -1 || this.requestStartDay.compareTo(endDay) == 1)
            return false;
        return true;
    }

}
