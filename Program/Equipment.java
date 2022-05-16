import java.util.ArrayList;

public class Equipment implements Comparable<Equipment> {
    private String equipmentCode;
    private String equipmentName;
    private ArrayList<EquipmentSet> sets;

    public Equipment(String equipmentCode, String equipmentName) {
        this.equipmentCode = equipmentCode;
        this.equipmentName = equipmentName;
        sets = new ArrayList<>();
        Club.getInstance().addEqiupment(this);
    }

    @Override
    public String toString() {
        return String.format("%-5s%-16s%5s  %s", equipmentCode, equipmentName, sets.size(), loanDetails());
    }

    // Return the loan details about the eqipment set  when listEquipment command is execute
    // If no loan record return empty string
    public String loanDetails() {
        String info = "";
        boolean firstOutput = true;
        for (EquipmentSet eSet : sets) {
            if (!eSet.checkAvailable()) {
                if (!firstOutput)
                    info += ", ";
                info += eSet.memberInfoForBorrow(); // e.g: E1_1(001)
                firstOutput = false;
            }
        }

        if (info.isEmpty())
            return info;
        else
            return "(Borrowed set(s): " + info + ")";
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public String getEquipmentCode() {
        return this.equipmentCode;
    }

    public int getEquipmentSetSize() {
        return this.sets.size();
    }

    public boolean checkCodeInUse(String newEquipmemntCode) {
        return this.equipmentCode.equals(newEquipmemntCode);
    }

    public void addIntoEqiupmentSet(EquipmentSet eSet) {
        this.sets.add(eSet);
    }

    public void removeFromEquipmentSet(EquipmentSet eSet) {
        this.sets.remove(eSet);
    }

    // Find an equipment set which is avilable status
    public EquipmentSet getCanBorrowEquipmentSet(Member m) throws ExOverlapPeriods {
        for (EquipmentSet eSet : sets)
            if (eSet.checkAvailable())
                return eSet;
        return null;
    }

    // Get equipment set which is borrowed by that member
    public EquipmentSet getEquipmentSetBorrowedBy(Member m) {
        for (EquipmentSet eSet : sets)
            if (eSet.HasBorrowedBy(m))
                return eSet;
        return null;
    }

    // Get the equipment set which without any overlap period and available for the request period
    public EquipmentSet getCanRequestEquipmentSet(Member m, Day startDay, Day endDay) throws ExOverlapPeriods {
        for (EquipmentSet eSet : sets)
            if (eSet.canRequest(m, startDay, endDay))
                return eSet;
        return null;
    }

    // Get equipment set which is requested by that member
    public EquipmentSet getEquipmentSetRequestBy(Member m) {
        for (EquipmentSet eSet : sets)
            if (eSet.hasRequestedBy(m))
                return eSet;
        return null;
    }

    @Override
    public int compareTo(Equipment another) {
        return this.equipmentCode.compareTo(another.equipmentCode);
    }
    
    public static String getListingHeader() {
        return String.format("%-5s%-16s%7s", "Code", "Name", "#sets");
    }
    
    // Used for listEquipmentStatus
    // generate the status for each equipment set 
    public String getEquipmentStatus() {
        if (sets.size() > 0) {
            String status = "";
            for (EquipmentSet eSet : sets) {
                if (eSet.checkAvailable())
                    status += String.format("  %s \n    %s\n", eSet.getSetNumber(), "Current status: Available");
                else
                    status += String.format("  %s \n    %s\n", eSet.getSetNumber(), eSet.borrowedStatus());

                if (eSet.hasRequestRecord())
                    status += String.format("    %s\n", eSet.requestedStatus());
            }
            return status;
        }

        else
            return "  We do not have any sets for this equipment.\n";
    }
}
